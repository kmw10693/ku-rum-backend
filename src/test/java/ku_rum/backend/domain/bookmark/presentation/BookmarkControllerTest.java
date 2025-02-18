package ku_rum.backend.domain.bookmark.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.bookmark.application.BookmarkService;
import ku_rum.backend.domain.bookmark.dto.request.BookmarkSaveRequest;
import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.domain.NoticeStatus;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BookmarkControllerTest extends RestDocsTestSupport {

    @MockBean
    private BookmarkService bookmarkService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("북마크 추가 성공")
    @Test
    void addBookmarkSuccess() throws Exception {
        // given
        BookmarkSaveRequest request = new BookmarkSaveRequest("https://konkuk.ac.kr");

        doNothing().when(bookmarkService).addBookmark(request);

        // when & then
        mockMvc.perform(post("/api/v1/bookmarks/save")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("북마크에 성공하였습니다"))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("url")
                                        .type(JsonFieldType.STRING)
                                        .description("북마크할 주소 url")
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.NUMBER)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 값 (OK)"),
                                fieldWithPath("data")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 메시지")
                        )));
    }

    @DisplayName("사용자 북마크 조회 성공")
    @Test
    void getBookmarksSuccess() throws Exception {
        // Notice 객체 생성 시 NoticeCategory 포함
        Notice notice = Notice.builder()
                .title("Notice")
                .url("https://konkuk.ac.kr")
                .date("2024-11-08")
                .noticeStatus(NoticeStatus.GENERAL)
                .noticeCategory(NoticeCategory.AFFAIR)
                .build();

        NoticeSimpleResponse response = new NoticeSimpleResponse(notice);

        when(bookmarkService.getBookmarks()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/v1/bookmarks/find")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].title").value("Notice"))
                .andExpect(jsonPath("$.data[0].url").value("https://konkuk.ac.kr"))
                .andExpect(jsonPath("$.data[0].date").value("2024-11-08"))
                .andExpect(jsonPath("$.data[0].category").value("학사"))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.NUMBER)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 값 (OK)"),
                                fieldWithPath("data")
                                        .type(JsonFieldType.ARRAY)
                                        .description("성공 시 반환 데이터"),
                                fieldWithPath("data[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 반환 url"),
                                fieldWithPath("data[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 반환 제목"),
                                fieldWithPath("data[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 반환 날짜"),
                                fieldWithPath("data[].important")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("성공 시 반환 중요도"),
                                fieldWithPath("data[].category")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 반환 카테고리")
                        )));
    }

}
