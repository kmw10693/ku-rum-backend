package ku_rum.backend.domain.notice.domain;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.domain.notice.dto.response.RecentSearchTerm;
import ku_rum.backend.domain.notice.presentation.NoticeRecentController;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.log.domain.repository.ApiLogRepository;
import ku_rum.backend.global.security.JwtTokenProvider;
import ku_rum.backend.global.utils.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(NoticeRecentController.class)
@ActiveProfiles("test")
class NoticeRecentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private ApiLogRepository apiLogRepository;

    @MockBean
    private UserService userService;

    @DisplayName("공지사항 제목을 통한 검색 테스트")
    @Test
    @WithMockUser
    void searchNotices() throws Exception {
        List<NoticeSimpleResponse> mockResponse = List.of(
                new NoticeSimpleResponse("https://example.com/notice1", "공지 제목 1", "2025-03-20", "일반", false),
                new NoticeSimpleResponse("https://example.com/notice2", "공지 제목 2", "2025-03-19", "중요", true)
        );
        given(noticeService.searchNoticesByTitle(any(), any())).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/notices/search").param("searchTerm", "테스트"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("공지 제목 1"))
                .andExpect(jsonPath("$.data[1].title").value("공지 제목 2"));
    }

    @DisplayName("최근 검색어 목록 조회 테스트")
    @Test
    @WithMockUser
    void searchTerms() throws Exception {
        RecentSearchTerm mockResponse = new RecentSearchTerm(1L, List.of("검색어1", "검색어2"));
        given(noticeService.getRecentSearchTerms(any())).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/notices/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.searchTerms[0]").value("검색어1"))
                .andExpect(jsonPath("$.data.searchTerms[1]").value("검색어2"));
    }
}
