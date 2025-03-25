package ku_rum.backend.domain.notice.domain;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.domain.notice.presentation.NoticeViewController;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.log.domain.repository.ApiLogRepository;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.security.JwtTokenProvider;
import ku_rum.backend.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.json.JsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class NoticeViewControllerTest extends RestDocsTestSupport {

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private UserService userService;

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

    @DisplayName("공지사항 제목을 통한 검색 테스트")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void searchNotices() throws Exception {
        List<NoticeSimpleResponse> mockResponse = List.of(
                new NoticeSimpleResponse("https://example.com/notice1", "공지 제목 1", "2025-03-20", "일반", false),
                new NoticeSimpleResponse("https://example.com/notice2", "공지 제목 2", "2025-03-19", "중요", true)
        );

        given(noticeService.searchNoticesByTitle(any(), any())).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/notices/search")
                        .param("searchTerm", "공지")
                        .with(user(customUserDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("공지 제목 1"))
                .andExpect(jsonPath("$.data[1].title").value("공지 제목 2"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("공지사항 API")
                                        .description("공지사항 제목을 통한 검색")
                                        .responseFields(
                                                fieldWithPath("code").type(JsonType.NUMBER).description("성공시 반환 코드 (200)"),
                                                fieldWithPath("status").type(JsonType.STRING).description("상태 값 (OK)"),
                                                fieldWithPath("message").type(JsonType.STRING).description("상태 메시지 (OK)"),
                                                fieldWithPath("data[].url").type(JsonType.STRING).description("공지사항 URL"),
                                                fieldWithPath("data[].title").type(JsonType.STRING).description("공지사항 제목"),
                                                fieldWithPath("data[].date").type(JsonType.STRING).description("공지사항 날짜"),
                                                fieldWithPath("data[].category").type(JsonType.STRING).description("공지사항 카테고리"),
                                                fieldWithPath("data[].important").type(JsonType.BOOLEAN).description("중요 공지 여부")
                                        ).build())));
    }

    @DisplayName("공지사항 카테고리를 통한 검색 테스트")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void getNoticesByCategory() throws Exception {
        List<NoticeSimpleResponse> mockNotices = Arrays.asList(
                new NoticeSimpleResponse(
                        "https://example.com/notice1",
                        "[대학일자리+] 2025년 졸업생 맞춤형 취업컨설팅 & 잡매칭 프로그램",
                        "2025-03-21",
                        "취창업",
                        false
                ),
                new NoticeSimpleResponse(
                        "https://example.com/notice2",
                        "[고용노동부XLG전자] 2025 서울고용노동청과 함께하는 취업준비콘서트_일자리톡톡",
                        "2025-03-18",
                        "취창업",
                        true
                )
        );

        when(noticeService.findNoticesByCategory("취창업")).thenReturn(mockNotices);

        mockMvc.perform(get("/api/v1/notices")
                        .param("category", "취창업")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].url").value("https://example.com/notice1"))
                .andExpect(jsonPath("$.data[0].title").value("[대학일자리+] 2025년 졸업생 맞춤형 취업컨설팅 & 잡매칭 프로그램"))
                .andExpect(jsonPath("$.data[0].date").value("2025-03-21"))
                .andExpect(jsonPath("$.data[0].category").value("취창업"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("공지사항 API")
                                        .description("공지사항 카테고리별 검색")
                                        .responseFields(
                                                fieldWithPath("code").type(JsonType.NUMBER).description("성공시 반환 코드 (200)"),
                                                fieldWithPath("status").type(JsonType.STRING).description("상태 값 (OK)"),
                                                fieldWithPath("message").type(JsonType.STRING).description("상태 메시지 (OK)"),
                                                fieldWithPath("data[].url").type(JsonType.STRING).description("공지사항 URL"),
                                                fieldWithPath("data[].title").type(JsonType.STRING).description("공지사항 제목"),
                                                fieldWithPath("data[].date").type(JsonType.STRING).description("공지사항 날짜"),
                                                fieldWithPath("data[].category").type(JsonType.STRING).description("공지사항 카테고리"),
                                                fieldWithPath("data[].important").type(JsonType.BOOLEAN).description("중요 공지 여부")
                                        ).build())));
    }
}
