package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.domain.NoticeStatus;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static javax.management.openmbean.SimpleType.STRING;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class NoticeControllerTest extends RestDocsTestSupport {

    @MockBean
    private NoticeService noticeService;

    @DisplayName("공지사항 크롤링 성공")
    @Test
    void crawlNoticesSuccess() throws Exception {
        doNothing().when(noticeService).crawlAndSaveKonkukNotices();

        mockMvc.perform(post("/api/v1/notices/crawl/konkuk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("크롤링 작업이 시작되었습니다."))
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
                                        .type(JsonType.STRING)
                                        .description("성공 시 '크롤링 작업이 시작되었습니다.' 반환")
                        )));
    }

    @DisplayName("카테고리별 공지사항 조회 성공")
    @Test
    void getNoticesByCategorySuccess() throws Exception {
        NoticeSimpleResponse response = new NoticeSimpleResponse(
                Notice.builder()
                        .title("Notice Category Test")
                        .url("https://konkuk.ac.kr")
                        .date("2024-11-07")
                        .noticeStatus(NoticeStatus.GENERAL)
                        .noticeCategory(NoticeCategory.AFFAIR)
                        .build()
        );

        when(noticeService.findNoticesByCategory(NoticeCategory.AFFAIR))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/notices")
                        .param("category", "AFFAIR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].title").value("Notice Category Test"))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("category").description("크롤링할 카테고리")
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
                                fieldWithPath("data[].category")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 반환 카테고리"),
                                fieldWithPath("data[].important")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("성공 시 반환 중요도 여부")
                        )));
    }

    @DisplayName("검색어를 통한 공지사항 조회 성공")
    @Test
    void searchNoticesByTitleSuccess() throws Exception {
        NoticeSimpleResponse response = new NoticeSimpleResponse(
                Notice.builder()
                        .title("Notice Search Test")
                        .url("https://konkuk.ac.kr")
                        .date("2024-11-07")
                        .noticeStatus(NoticeStatus.GENERAL)
                        .noticeCategory(NoticeCategory.AFFAIR)
                        .build()
        );

        when(noticeService.searchNoticesByTitle("Search"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/notices/search")
                        .param("searchTerm", "Search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].title").value("Notice Search Test"))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("searchTerm").description("크롤링할 검색어")
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
                                fieldWithPath("data[].category")
                                        .type(JsonFieldType.STRING)
                                        .description("성공 시 반환 카테고리"),
                                fieldWithPath("data[].important")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("성공 시 반환 중요도 여부")
                        )));
    }

}
