package ku_rum.backend.domain.user.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.friend.application.FriendReportService;
import ku_rum.backend.domain.friend.domain.repository.FriendBlockRepository;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepositoryImpl;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.application.UserValidator;
import ku_rum.backend.domain.user.dto.request.UserLocationRequest;
import ku_rum.backend.domain.user.dto.request.UserLocationShareStartRequest;
import ku_rum.backend.domain.user.dto.response.UserLocationResponse;
import ku_rum.backend.domain.user.dto.response.UserLocationShareStartResponse;
import ku_rum.backend.domain.user.dto.response.UserShareActiveResponse;
import ku_rum.backend.global.batch.BatchScheduler;
import ku_rum.backend.global.domain.repository.ApiLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserMapControllerTest extends RestDocsTestSupport {
    @MockBean
    private UserService userService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private NoticeRepositoryImpl noticeRepository;

    @MockBean
    private ApiLogRepository apiLogRepository;

    @MockBean
    private FriendReportService friendManageService;

    @MockBean
    private FriendBlockRepository friendBlockRepository;

    @DisplayName("위치 공유 상태를 조회한다.")
    @Test
    @WithMockUser
    void getUserShareActiveStatus() throws Exception {
        // given
        given(userService.isShareActive()).willReturn(new UserShareActiveResponse(false));

        // when then
        mockMvc.perform(get("/api/v1/map")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.active").value(false))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("1. 현재 위치 공유 상태 반환")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonType.STRING).description("응답 코드 (예: 200)"),
                                        fieldWithPath("status").type(JsonType.STRING).description("응답 상태 (예: OK)"),
                                        fieldWithPath("message").type(JsonType.STRING).description("응답 메시지 (예: OK)"),
                                        fieldWithPath("data.active").type(JsonType.BOOLEAN).description("현재 위치 공유 활성화 여부")
                                )
                                .build()
                )));
    }

    @DisplayName("현재 위치를 공유하고, 해당 위치의 단과대 정보를 반환한다.")
    @Test
    @WithMockUser
    void shareCurrentLocation() throws Exception {
        // given
        UserLocationRequest request = new UserLocationRequest(37.5665, 126.9780);
        UserLocationResponse response = new UserLocationResponse("상허기념도서관");

        given(userService.getUserLocation(any(UserLocationRequest.class)))
                .willReturn(response);

        // when then
        mockMvc.perform(post("/api/v1/map/share")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.placePointed").value("상허기념도서관"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("2. 현재 위치 공유 요청 후, 위치에 해당하는 단과대 반환")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .requestFields(
                                        fieldWithPath("latitude").type(JsonType.NUMBER).description("위도"),
                                        fieldWithPath("longitude").type(JsonType.NUMBER).description("경도")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonType.STRING).description("응답 코드 (예: 200)"),
                                        fieldWithPath("status").type(JsonType.STRING).description("응답 상태 (예: OK)"),
                                        fieldWithPath("message").type(JsonType.STRING).description("응답 메시지 (예: OK)"),
                                        fieldWithPath("data.placePointed").type(JsonType.STRING).description("해당 위치에 해당하는 단과대 이름")
                                )
                                .build()
                )));
    }

    @DisplayName("위치 공유를 시작하고 상태를 활성화한다.")
    @Test
    @WithMockUser
    void startShareLocation() throws Exception {
        // given
        UserLocationShareStartRequest request = new UserLocationShareStartRequest("상허기념도서관");
        UserLocationShareStartResponse response = new UserLocationShareStartResponse("상허기념도서관", true);

        given(userService.startShareLocation(any(UserLocationShareStartRequest.class)))
                .willReturn(response);

        // when then
        mockMvc.perform(post("/api/v1/map/shareStart")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.placePointed").value("상허기념도서관"))
                .andExpect(jsonPath("$.data.active").value(true))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("3. 위치 공유를 시작하고 상태를 활성화함")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .requestFields(
                                        fieldWithPath("placePointed").type(JsonType.STRING).description("공유 시작할 위치의 단과대 명칭")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonType.STRING).description("응답 코드 (예: 200)"),
                                        fieldWithPath("status").type(JsonType.STRING).description("응답 상태 (예: OK)"),
                                        fieldWithPath("message").type(JsonType.STRING).description("응답 메시지 (예: OK)"),
                                        fieldWithPath("data.placePointed").type(JsonType.STRING).description("공유한 위치의 단과대 명칭"),
                                        fieldWithPath("data.active").type(JsonType.BOOLEAN).description("위치 공유 활성화 여부")
                                )
                                .build()
                )));
    }

    @DisplayName("위치 공유를 중단한다.")
    @Test
    @WithMockUser
    void stopSharingLocation() throws Exception {
        // given
        given(userService.changeToNotActive()).willReturn(new UserShareActiveResponse(false));

        // when then
        mockMvc.perform(get("/api/v1/map/notshare")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.active").value(false))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("4. 위치 공유 중단 요청")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonType.STRING).description("응답 코드 (예: 200)"),
                                        fieldWithPath("status").type(JsonType.STRING).description("응답 상태 (예: OK)"),
                                        fieldWithPath("message").type(JsonType.STRING).description("응답 메시지 (예: OK)"),
                                        fieldWithPath("data.active").type(JsonType.BOOLEAN).description("위치 공유 활성화 여부 (항상 false)")
                                )
                                .build()
                )));
    }






}
