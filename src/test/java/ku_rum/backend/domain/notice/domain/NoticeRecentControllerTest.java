package ku_rum.backend.domain.notice.domain;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.domain.notice.dto.response.RecentSearchTerm;
import ku_rum.backend.global.batch.BatchConfig;
import ku_rum.backend.global.domain.repository.ApiLogRepository;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.security.JwtTokenProvider;
import ku_rum.backend.global.utill.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@EnableScheduling
class NoticeRecentControllerTest  extends RestDocsTestSupport {

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private BatchConfig batchConfig;

    @MockBean
    private JobLauncher jobLauncher;

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
        securityContext.setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("최근 검색어 목록 조회 테스트")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void searchTerms() throws Exception {
        RecentSearchTerm mockResponse = new RecentSearchTerm(1L, List.of("검색어1", "검색어2"));
        given(noticeService.getRecentSearchTerms(any())).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/notices/recent")
                        .with(user(customUserDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.searchedList[0]").value("검색어1"))
                .andExpect(jsonPath("$.data.searchedList[1]").value("검색어2"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("공지사항 API")
                                        .description("최근 검색 단어 불러오기")
                                        .responseFields(
                                            fieldWithPath("status").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                            fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                            fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                            fieldWithPath("data.searchedList").type(JsonFieldType.ARRAY).description("최근 검색어 목록")
                                        ).build())));
    }

    @DisplayName("최근 공지사항 5개 조회 테스트")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void recent5Notices() throws Exception {
        // Mock 데이터 준비
        List<NoticeSimpleResponse> mockResponse = List.of(
                new NoticeSimpleResponse("https://www.konkuk.ac.kr/bbs/konkuk/243/1142471/artclView.do", "2025. 3. 1.자 강사/비전임 2차 공개채용 최종합격자 발표", "2025.01.24", "채용", false),
                new NoticeSimpleResponse("https://www.konkuk.ac.kr/bbs/konkuk/243/1148491/artclView.do", "건국대학교 교수학습센터 전문직(연구행정직) 채용 공고", "2025.04.11", "채용", false),
                new NoticeSimpleResponse("https://www.konkuk.ac.kr/bbs/konkuk/243/1148581/artclView.do", "수의과대학 행정지원직 채용 공고", "2025.04.14", "채용", false),
                new NoticeSimpleResponse("https://www.konkuk.ac.kr/bbs/konkuk/243/1148795/artclView.do", "건국대학교 경영대학 행정실 행정지원직 채용 공고", "2025.04.17", "채용", false),
                new NoticeSimpleResponse("https://www.konkuk.ac.kr/bbs/konkuk/243/1148862/artclView.do", "건국대학교 법학전문대학원 직원(행정지원직) 채용", "2025.04.18", "채용", false)
        );

        given(noticeService.getRecent5Notices()).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/notices/recent/5notices")
                        .with(user(customUserDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("2025. 3. 1.자 강사/비전임 2차 공개채용 최종합격자 발표"))
                .andExpect(jsonPath("$.data[1].title").value("건국대학교 교수학습센터 전문직(연구행정직) 채용 공고"))
                .andExpect(jsonPath("$.data.length()").value(5))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("공지사항 API")
                                        .description("최근 공지사항 5개 가져오기")
                                        .responseFields(
                                                fieldWithPath("status").type(JsonFieldType.STRING).description("요청 성공 여부"),
                                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data[].url").type(JsonFieldType.STRING).description("공지사항 URL"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                                fieldWithPath("data[].date").type(JsonFieldType.STRING).description("공지사항 날짜"),
                                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("공지사항 카테고리"),
                                                fieldWithPath("data[].important").type(JsonFieldType.BOOLEAN).description("중요 공지 여부")
                                        )
                                        .build()
                        )
                ));
    }



}