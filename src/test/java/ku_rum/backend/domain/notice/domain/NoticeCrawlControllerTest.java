/*
package ku_rum.backend.domain.notice.domain;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.presentation.NoticeCrawlController;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.log.domain.repository.ApiLogRepository;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.security.JwtTokenProvider;
import ku_rum.backend.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static ku_rum.backend.domain.notice.dto.response.CrawlingResponse.START_CRAWLING;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(NoticeCrawlController.class)
@ActiveProfiles("test")
class NoticeCrawlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private ApiLogRepository apiLogRepository;

    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        customUserDetails = Mockito.mock(CustomUserDetails.class);
        given(customUserDetails.getUserId()).willReturn(1L);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(customUserDetails, null, List.of()));
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("건국대학교 공지사항 크롤링 요청 테스트")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void crawlKonkukNotices() throws Exception {
        doNothing().when(noticeService).crawlAndSaveKonkukNotices();

        mockMvc.perform(post("/api/v1/notices/crawl/konkuk")
                        .with(user(customUserDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(START_CRAWLING.getMessage()));

        verify(noticeService, Mockito.times(1)).crawlAndSaveKonkukNotices();
    }
}
*/
