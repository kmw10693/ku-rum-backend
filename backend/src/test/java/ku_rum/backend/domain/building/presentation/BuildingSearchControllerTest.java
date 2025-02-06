package ku_rum.backend.domain.building.presentation;

import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.config.redis.RedisUtil;
import ku_rum.backend.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(BuildingSearchController.class)
class BuildingSearchControllerTest extends RestDocsTestSupport {

  @MockBean
  private BuildingSearchService buildingSearchService;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private RedisUtil redisUtil;

  @DisplayName("학교의 모든 건물정보를 출력한다.")
  @Test
  @WithMockUser
  void viewAll() throws Exception {
    // given (Mock 데이터 설정)
    List<BuildingResponse> mockBuildings = List.of(
            new BuildingResponse(16L, "공학관", 21L, "공",
                    BigDecimal.valueOf(37.5418220000000),BigDecimal.valueOf(127.0788450000000)),
            new BuildingResponse(1L, "경영관", 2L, "경영",
                    BigDecimal.valueOf(37.5444190000000), BigDecimal.valueOf(127.0763700000000))
    );
    given(buildingSearchService.findAllBuildings()).willReturn(mockBuildings);

    //when then
    mockMvc.perform(get("/api/v1/buildings/view")
            .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))

            .andExpect(jsonPath("$.data[0].buildingId").value(16))
            .andExpect(jsonPath("$.data[0].buildingName").value("공학관"))
            .andExpect(jsonPath("$.data[0].buildingNumber").value(21))
            .andExpect(jsonPath("$.data[0].bulidingAbbreviation").value("공"))
            .andExpect(jsonPath("$.data[0].latitude").value(37.541822))
            .andExpect(jsonPath("$.data[0].longtitude").value(127.078845))
            .andExpect(jsonPath("$.data[1].buildingId").value(1))
            .andExpect(jsonPath("$.data[1].buildingName").value("경영관"))
            .andExpect(jsonPath("$.data[1].buildingNumber").value(2))
            .andExpect(jsonPath("$.data[1].bulidingAbbreviation").value("경영"))
            .andExpect(jsonPath("$.data[1].latitude").value(37.544419))
            .andExpect(jsonPath("$.data[1].longtitude").value(127.076370))

            .andDo(restDocs.document(
                    responseFields(
                            fieldWithPath("code")
                                    .type(JsonType.NUMBER)
                                    .description("성공시 반환 코드 (200)"),
                            fieldWithPath("status")
                                    .type(JsonType.STRING)
                                    .description("올바른 인증코드 시 상태 값 (OK)"),
                            fieldWithPath("message")
                                    .type(JsonType.STRING)
                                    .description("올바른 인증코드 시 메시지 (OK)"),
                            fieldWithPath("data[].buildingId")
                                    .type(JsonType.NUMBER).description("빌딩 ID"),
                            fieldWithPath("data[].buildingName")
                                    .type(JsonType.STRING)
                                    .description("빌딩 이름"),
                            fieldWithPath("data[].buildingNumber")
                                    .type(JsonType.NUMBER)
                                    .description("빌딩 번호"),
                            fieldWithPath("data[].bulidingAbbreviation")
                                    .type(JsonType.STRING)
                                    .description("빌딩 약어"),
                            fieldWithPath("data[].latitude")
                                    .type(JsonType.NUMBER)
                                    .description("위도"),
                            fieldWithPath("data[].longtitude")
                                    .type(JsonType.NUMBER)
                                    .description("경도")
                    )));


  }


  @DisplayName("특정 건물번호로 해당 건물정보를 출력한다.")
  @Test
  @WithMockUser
  void viewBuildingByNumber() throws Exception {
    // given (Mock 데이터 설정)
    List<BuildingResponse> mockBuildings = List.of(
            new BuildingResponse(16L, "공학관", 21L, "공",
                    BigDecimal.valueOf(37.5418220000000),BigDecimal.valueOf(127.0788450000000)),
            new BuildingResponse(1L, "경영관", 2L, "경영",
                    BigDecimal.valueOf(37.5444190000000), BigDecimal.valueOf(127.0763700000000))
    );
    given(buildingSearchService.viewBuildingByNumber(21L)).willReturn(mockBuildings.get(0));

    //when then
    mockMvc.perform(get("/api/v1/buildings/view/searchNumber?number=21")
            .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))

            .andExpect(jsonPath("$.data.buildingId").value(16))
            .andExpect(jsonPath("$.data.buildingName").value("공학관"))
            .andExpect(jsonPath("$.data.buildingNumber").value(21))
            .andExpect(jsonPath("$.data.bulidingAbbreviation").value("공"))
            .andExpect(jsonPath("$.data.latitude").value(37.541822))
            .andExpect(jsonPath("$.data.longtitude").value(127.078845))

            .andDo(restDocs.document(
                    responseFields(
                            fieldWithPath("code")
                                    .type(JsonType.NUMBER)
                                    .description("성공시 반환 코드 (200)"),
                            fieldWithPath("status")
                                    .type(JsonType.STRING)
                                    .description("올바른 인증코드 시 상태 값 (OK)"),
                            fieldWithPath("message")
                                    .type(JsonType.STRING)
                                    .description("올바른 인증코드 시 메시지 (OK)"),
                            fieldWithPath("data.buildingId")
                                    .type(JsonType.NUMBER).description("빌딩 ID"),
                            fieldWithPath("data.buildingName")
                                    .type(JsonType.STRING)
                                    .description("빌딩 이름"),
                            fieldWithPath("data.buildingNumber")
                                    .type(JsonType.NUMBER)
                                    .description("빌딩 번호"),
                            fieldWithPath("data.bulidingAbbreviation")
                                    .type(JsonType.STRING)
                                    .description("빌딩 약어"),
                            fieldWithPath("data.latitude")
                                    .type(JsonType.NUMBER)
                                    .description("위도"),
                            fieldWithPath("data.longtitude")
                                    .type(JsonType.NUMBER)
                                    .description("경도")
                    )));



  }


}