package ku_rum.backend.domain.category.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.auth.application.TokenBlacklistService;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;
import ku_rum.backend.domain.category.application.CategoryViewService;
import ku_rum.backend.domain.category.dto.response.CategoryViewPlaceResponse;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableScheduling
@ActiveProfiles("test")
class CategoryViewControllerTest extends RestDocsTestSupport {

    @MockBean
    private CategoryViewService categoryViewService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @DisplayName("카테고리 이름으로 카테고리 내 해당되는 장소들을 조회한다.")
    @Test
    @WithMockUser
    void getPlacesByCategoryName() throws Exception {
        // given
        Building building1 = Building.of("공학관점", 11L, "공",
                BigDecimal.valueOf(37.541004), BigDecimal.valueOf(127.074197));
        ReflectionTestUtils.setField(building1, "id", 100L); // ID 값 설정

        Building building2 = Building.of("경영관점", 1L, "경",
                BigDecimal.valueOf(37.543075), BigDecimal.valueOf(127.075067));
        ReflectionTestUtils.setField(building2, "id", 101L); // ID 값 설정

        List<CategoryViewPlaceResponse> mockResponse = List.of(
                new CategoryViewPlaceResponse(
                        29L, "레스티오-공학관점",
                        BigDecimal.valueOf(37.541651), BigDecimal.valueOf(127.078702),
                        BuildingViewResponse.from(building1)
                ),
                new CategoryViewPlaceResponse(
                        30L, "레스티오-경영관점",
                        BigDecimal.valueOf(37.544476), BigDecimal.valueOf(127.076513),
                        BuildingViewResponse.from(building2)
                )
        );

        given(categoryViewService.getPlacesByCategoryName("레스티오")).willReturn(mockResponse);

        // when then
        mockMvc.perform(get("/api/v1/categories/{categoryName}/places", "레스티오") // URL 템플릿을 사용
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].placeId").value(29))
                .andExpect(jsonPath("$.data[0].name").value("레스티오-공학관점"))
                .andExpect(jsonPath("$.data[0].latitude").value(37.541651))
                .andExpect(jsonPath("$.data[0].longitude").value(127.078702))
                .andExpect(jsonPath("$.data[0].building.id").value(100))  // id가 존재하지 않음을 검증
                .andExpect(jsonPath("$.data[0].building.name").value("공학관점"))
                .andExpect(jsonPath("$.data[0].building.abbreviation").value("공"))
                .andExpect(jsonPath("$.data[0].building.number").value(11))

                .andExpect(jsonPath("$.data[1].placeId").value(30))
                .andExpect(jsonPath("$.data[1].name").value("레스티오-경영관점"))
                .andExpect(jsonPath("$.data[1].latitude").value(37.544476))
                .andExpect(jsonPath("$.data[1].longitude").value(127.076513))
                .andExpect(jsonPath("$.data[1].building.id").value(101) ) // id가 존재하지 않음을 검증
                .andExpect(jsonPath("$.data[1].building.name").value("경영관점"))
                .andExpect(jsonPath("$.data[1].building.abbreviation").value("경"))
                .andExpect(jsonPath("$.data[1].building.number").value(1))

                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("카테고리 관련 API")
                                .description("카테고리 이름으로 장소 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 ID"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data[].latitude").type(JsonFieldType.NUMBER).description("장소 위도"),
                                        fieldWithPath("data[].longitude").type(JsonFieldType.NUMBER).description("장소 경도"),
                                        fieldWithPath("data[].building.id").type(JsonFieldType.NUMBER)
                                                .optional()
                                                .description("건물 ID"),
                                        fieldWithPath("data[].building.name").type(JsonFieldType.STRING).description("건물 이름"),
                                        fieldWithPath("data[].building.abbreviation").type(JsonFieldType.STRING).description("건물 약어"),
                                        fieldWithPath("data[].building.number").type(JsonFieldType.NUMBER).description("건물 번호"),
                                        fieldWithPath("data[].building.latitude").type(JsonFieldType.NUMBER).description("건물 위도"),
                                        fieldWithPath("data[].building.longitude").type(JsonFieldType.NUMBER).description("건물 경도")
                                ).build()
                        )
                ));

    }
}
