package ku_rum.backend.domain.place.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.auth.application.TokenBlacklistService;
import ku_rum.backend.domain.place.application.PlaceViewService;
import ku_rum.backend.domain.place.dto.response.PlaceDetailView;
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

import java.math.BigDecimal;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EnableScheduling
@ActiveProfiles("test")
class PlaceViewControllerTest extends RestDocsTestSupport {

    @MockBean
    private PlaceViewService placeViewService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @DisplayName("장소 ID로 상세 정보를 조회한다.")
    @Test
    @WithMockUser
    void getPlaceDetail() throws Exception {
        // given
        Long placeId = 10L;
        PlaceDetailView mockResponse = new PlaceDetailView(
                placeId,
                "예술디자인대학",
                "예",
                "예디대, 예디대, 예술디자인대학 설명 예시예시",
                BigDecimal.valueOf(37.544419), BigDecimal.valueOf(127.073994),
                5L,
                "예술문화관",
                2L,
                "단과대"
        );

        given(placeViewService.getPlaceDetail(placeId)).willReturn(mockResponse);

        // when then
        mockMvc.perform(get("/api/v1/places/placedetail/{id}", placeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.name").value("예술디자인대학"))
                .andExpect(jsonPath("$.data.subName").value("예"))
                .andExpect(jsonPath("$.data.text").value("예디대, 예디대, 예술디자인대학 설명 예시예시"))
                .andExpect(jsonPath("$.data.latitude").value(37.544419))
                .andExpect(jsonPath("$.data.longitude").value(127.073994))
                .andExpect(jsonPath("$.data.buildingId").value(5))
                .andExpect(jsonPath("$.data.buildingName").value("예술문화관"))
                .andExpect(jsonPath("$.data.categoryId").value(2))
                .andExpect(jsonPath("$.data.categoryName").value("단과대"))

                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("장소 관련 API")
                                .description("장소 ID로 상세 정보 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("장소 ID"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data.subName").type(JsonFieldType.STRING).description("장소 보조 이름"),
                                        fieldWithPath("data.text").type(JsonFieldType.STRING).description("장소 설명"),
                                        fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("경도"),
                                        fieldWithPath("data.buildingId").type(JsonFieldType.NUMBER).description("건물 ID"),
                                        fieldWithPath("data.buildingName").type(JsonFieldType.STRING).description("건물 이름"),
                                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리 이름")
                                ).build()
                        )
                ));
    }
}
