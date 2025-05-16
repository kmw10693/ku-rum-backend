package ku_rum.backend.domain.bookmark.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.auth.application.TokenBlacklistService;
import ku_rum.backend.domain.bookmark.application.BookmarkService;
import ku_rum.backend.domain.bookmark.dto.response.BookmarkSimpleResponse;
import ku_rum.backend.global.batch.BatchScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EnableScheduling
@ActiveProfiles("test")
class BookmarkRecentControllerTest extends RestDocsTestSupport {

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @MockBean
    private BatchScheduler batchScheduler;

    @Test
    @DisplayName("최근 북마크 5개 조회 성공")
    @WithMockUser
    void getRecent5Bookmarks_success() throws Exception {
        // given
        List<BookmarkSimpleResponse> dummyResponse = List.of(
                new BookmarkSimpleResponse(1L, "공지사항 제목1", "https://example.com/notice1", LocalDateTime.of(2024, 5, 1, 12, 0)),
                new BookmarkSimpleResponse(2L, "공지사항 제목2", "https://example.com/notice2", LocalDateTime.of(2024, 5, 2, 13, 30))
        );

        given(bookmarkService.getRecent5bookmarks()).willReturn(dummyResponse);

        // when & then
        mockMvc.perform(get("/api/v1/bookmarks/recent/5bookmarks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].bookmarkId").value(1))
                .andExpect(jsonPath("$.data[0].noticeTitle").value("공지사항 제목1"))
                .andExpect(jsonPath("$.data[0].noticeUrl").value("https://example.com/notice1"))
                .andExpect(jsonPath("$.data[0].createdAt").exists())
                .andExpect(jsonPath("$.data[1].bookmarkId").value(2))
                .andExpect(jsonPath("$.data[1].noticeTitle").value("공지사항 제목2"))
                .andExpect(jsonPath("$.data[1].noticeUrl").value("https://example.com/notice2"))
                .andExpect(jsonPath("$.data[1].createdAt").exists())

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("북마크 API")
                                        .description("최근 북마크 5개 조회")
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data[].bookmarkId").type(JsonFieldType.NUMBER).description("북마크 ID"),
                                                fieldWithPath("data[].noticeTitle").type(JsonFieldType.STRING).description("북마크된 공지사항 제목"),
                                                fieldWithPath("data[].noticeUrl").type(JsonFieldType.STRING).description("공지사항 URL"),
                                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("북마크 생성 시간")
                                        ).build()
                        )));
    }
}
