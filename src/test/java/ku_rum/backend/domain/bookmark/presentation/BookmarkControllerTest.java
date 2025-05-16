package ku_rum.backend.domain.bookmark.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.auth.application.TokenBlacklistService;
import ku_rum.backend.domain.bookmark.application.BookmarkService;
import ku_rum.backend.domain.bookmark.dto.request.BookmarkSaveRequest;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.global.batch.BatchScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@EnableScheduling
@ActiveProfiles("test")
public class BookmarkControllerTest extends RestDocsTestSupport {

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @MockBean
    private BatchScheduler batchScheduler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("북마크 저장 API 성공")
    @WithMockUser
    void addBookmark_success() throws Exception {
        BookmarkSaveRequest request = BookmarkSaveRequest.builder()
                .url("https://example.com/notice1")
                .build();

        mockMvc.perform(post("/api/v1/bookmarks/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("북마크 API")
                                        .description("사용자 북마크 저장")
                                        .requestFields(
                                                fieldWithPath("url").description("북마크할 공지사항 URL")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").description("응답 코드"),
                                                fieldWithPath("status").description("응답 상태"),
                                                fieldWithPath("message").description("응답 메시지"),
                                                fieldWithPath("data").description("응답 데이터(성공 메시지)")
                                        )
                                        .build()
                        )
                ));
    }


    @Test
    @DisplayName("모든 북마크 조회 API 성공")
    @WithMockUser
    void getBookmarks_success() throws Exception {
        List<NoticeSimpleResponse> dummyResponse = List.of(
                NoticeSimpleResponse.builder()
                        .url("https://example.com/notice1")
                        .title("공지사항 제목1")
                        .date("2024-05-01")
                        .category("공지")
                        .isImportant(true)
                        .build(),
                NoticeSimpleResponse.builder()
                        .url("https://example.com/notice2")
                        .title("공지사항 제목2")
                        .date("2024-05-02")
                        .category("일반")
                        .isImportant(false)
                        .build()
        );

        given(bookmarkService.getBookmarks()).willReturn(dummyResponse);

        mockMvc.perform(get("/api/v1/bookmarks/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].url").value("https://example.com/notice1"))
                .andExpect(jsonPath("$.data[0].title").value("공지사항 제목1"))
                .andExpect(jsonPath("$.data[0].date").value("2024-05-01"))
                .andExpect(jsonPath("$.data[0].category").value("공지"))
                .andExpect(jsonPath("$.data[0].important").value(true))
                .andExpect(jsonPath("$.data[1].url").value("https://example.com/notice2"))
                .andExpect(jsonPath("$.data[1].title").value("공지사항 제목2"))
                .andExpect(jsonPath("$.data[1].date").value("2024-05-02"))
                .andExpect(jsonPath("$.data[1].category").value("일반"))
                .andExpect(jsonPath("$.data[1].important").value(false))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("북마크 API")
                                        .description("사용자 북마크 전체 조회")
                                        .responseFields(
                                                fieldWithPath("code").description("응답 코드"),
                                                fieldWithPath("status").description("응답 상태"),
                                                fieldWithPath("message").description("응답 메시지"),
                                                fieldWithPath("data[].url").description("공지사항 URL"),
                                                fieldWithPath("data[].title").description("공지사항 제목"),
                                                fieldWithPath("data[].date").description("공지사항 날짜"),
                                                fieldWithPath("data[].category").description("공지사항 카테고리"),
                                                fieldWithPath("data[].important").description("중요 공지 여부")
                                        ).build()
                        )));
    }
}
