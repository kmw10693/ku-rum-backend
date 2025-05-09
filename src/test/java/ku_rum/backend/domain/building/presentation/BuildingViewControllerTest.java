package ku_rum.backend.domain.building.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.building.application.BuildingViewService;
import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.application.UserValidator;
import ku_rum.backend.global.domain.repository.ApiLogRepository;
import ku_rum.backend.global.security.JwtTokenProvider;
import ku_rum.backend.global.utill.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(BuildingViewController.class)
@ActiveProfiles("test")
class BuildingViewControllerTest extends RestDocsTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildingViewService buildingViewService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private ApiLogRepository apiLogRepository;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private Job job; // 실제 배치 Job

    @MockBean
    private JobRepository jobRepository; // JobRepository Mock

    @MockBean
    private JobExplorer jobExplorer; // JobExplorer Mock

    @MockBean
    private JobOperator jobOperator; // JobExplorer Mock


    @DisplayName("특정 건물번호로 해당 건물정보를 출력한다.")
    @Test
    @WithMockUser
    void viewBuildingByNumber() throws Exception {
        // given (Mock 데이터 설정)
        List<BuildingViewResponse> mockBuildings = List.of(
                new BuildingViewResponse(16L, "공", "공학관", 21L,
                        BigDecimal.valueOf(37.5418220000000), BigDecimal.valueOf(127.0788450000000))
        );
        given(buildingViewService.getBuildingByNumber(21)).willReturn(mockBuildings.get(0));

        // when then
        mockMvc.perform(get("/api/v1/buildings/search/{number}", 21)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(16))
                .andExpect(jsonPath("$.data.name").value("공학관"))
                .andExpect(jsonPath("$.data.number").value(21))
                .andExpect(jsonPath("$.data.abbreviation").value("공"))
                .andExpect(jsonPath("$.data.latitude").value(37.541822))
                .andExpect(jsonPath("$.data.longitude").value(127.078845))

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("빌딩 관련 API")
                                        .description("특정 건물번호로 건물 정보 출력")
                                        .responseFields(
                                                fieldWithPath("code").type(JsonType.NUMBER).description("성공시 반환 코드 (200)"),
                                                fieldWithPath("status").type(JsonType.STRING).description("올바른 인증코드 시 상태 값 (OK)"),
                                                fieldWithPath("message").type(JsonType.STRING).description("올바른 인증코드 시 메시지 (OK)"),
                                                fieldWithPath("data.id").type(JsonType.NUMBER).description("빌딩 ID"),
                                                fieldWithPath("data.name").type(JsonType.STRING).description("빌딩 이름"),
                                                fieldWithPath("data.number").type(JsonType.NUMBER).description("빌딩 번호"),
                                                fieldWithPath("data.abbreviation").type(JsonType.STRING).description("빌딩 약어"),
                                                fieldWithPath("data.latitude").type(JsonType.NUMBER).description("위도"),
                                                fieldWithPath("data.longitude").type(JsonType.NUMBER).description("경도")
                                        ).build())));

    }

    @DisplayName("특정 건물이름/줄임말로 해당 건물정보를 출력한다.")
    @Test
    @WithMockUser
    void viewBuildingByName() throws Exception {
        // given (Mock 데이터 설정)
        List<BuildingViewResponse> mockBuildings = List.of(
                new BuildingViewResponse(16L, "공", "공학관", 21L,
                        BigDecimal.valueOf(37.5418220000000), BigDecimal.valueOf(127.0788450000000)),
                new BuildingViewResponse(15L, "신공", "신공학관", 22L,
                        BigDecimal.valueOf(37.5418120000000), BigDecimal.valueOf(127.0788460000000))
        );
        given(buildingViewService.searchBuildings("공"))
                .willReturn(List.of(mockBuildings.get(0), mockBuildings.get(1)));

        // when then
        mockMvc.perform(get("/api/v1/buildings/search?name=공")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))

                .andExpect(jsonPath("$.data[0].id").value(16))
                .andExpect(jsonPath("$.data[0].name").value("공학관"))
                .andExpect(jsonPath("$.data[0].number").value(21))
                .andExpect(jsonPath("$.data[0].abbreviation").value("공"))
                .andExpect(jsonPath("$.data[0].latitude").value(37.541822))
                .andExpect(jsonPath("$.data[0].longitude").value(127.078845))

                .andExpect(jsonPath("$.data[1].id").value(15))
                .andExpect(jsonPath("$.data[1].name").value("신공학관"))
                .andExpect(jsonPath("$.data[1].number").value(22))
                .andExpect(jsonPath("$.data[1].abbreviation").value("신공"))
                .andExpect(jsonPath("$.data[1].latitude").value(37.541812))
                .andExpect(jsonPath("$.data[1].longitude").value(127.078846))

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("빌딩 관련 API")
                                        .description("건물이름/줄임말로 건물정보 검색")
                                        .responseFields(
                                                fieldWithPath("code").type(JsonType.NUMBER).description("성공시 반환 코드 (200)"),
                                                fieldWithPath("status").type(JsonType.STRING).description("올바른 인증코드 시 상태 값 (OK)"),
                                                fieldWithPath("message").type(JsonType.STRING).description("올바른 인증코드 시 메시지 (OK)"),
                                                fieldWithPath("data[0].id").type(JsonType.NUMBER).description("첫 번째 빌딩 ID"),
                                                fieldWithPath("data[0].name").type(JsonType.STRING).description("첫 번째 빌딩 이름"),
                                                fieldWithPath("data[0].number").type(JsonType.NUMBER).description("첫 번째 빌딩 번호"),
                                                fieldWithPath("data[0].abbreviation").type(JsonType.STRING).description("첫 번째 빌딩 약어"),
                                                fieldWithPath("data[0].latitude").type(JsonType.NUMBER).description("첫 번째 빌딩 위도"),
                                                fieldWithPath("data[0].longitude").type(JsonType.NUMBER).description("첫 번째 빌딩 경도"),
                                                fieldWithPath("data[1].id").type(JsonType.NUMBER).description("두 번째 빌딩 ID"),
                                                fieldWithPath("data[1].name").type(JsonType.STRING).description("두 번째 빌딩 이름"),
                                                fieldWithPath("data[1].number").type(JsonType.NUMBER).description("두 번째 빌딩 번호"),
                                                fieldWithPath("data[1].abbreviation").type(JsonType.STRING).description("두 번째 빌딩 약어"),
                                                fieldWithPath("data[1].latitude").type(JsonType.NUMBER).description("두 번째 빌딩 위도"),
                                                fieldWithPath("data[1].longitude").type(JsonType.NUMBER).description("두 번째 빌딩 경도")
                                        ).build())));
    }



}