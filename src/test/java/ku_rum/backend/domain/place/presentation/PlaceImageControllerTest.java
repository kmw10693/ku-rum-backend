package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.auth.application.TokenBlacklistService;
import ku_rum.backend.domain.place.application.PlaceImageService;
import ku_rum.backend.global.batch.BatchScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@EnableScheduling
@ActiveProfiles("test")
class PlaceImageControllerTest extends RestDocsTestSupport{
    @MockBean
    private PlaceImageService placeImageService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @Test
    @WithMockUser
    @DisplayName("특정 장소 ID로 이미지 URL 목록 조회 테스트 (REST Docs 포함)")
    void getImageByFileNameTest() throws Exception {
        Long placeId = 10L;

        List<String> mockUrls = List.of(
                "https://kuroom.s3.ap-northeast-2.amazonaws.com/places/10/?X-Amz-Algorithm=...",
                "https://kuroom.s3.ap-northeast-2.amazonaws.com/places/10/한글파일명.png?X-Amz-Algorithm=..."
        );

        given(placeImageService.getPresignedImageUrls(placeId)).willReturn(mockUrls);

        mockMvc.perform(get("/api/v1/places/imgs/id={placeId}", placeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value(mockUrls.get(0)))
                .andExpect(jsonPath("$.data[1]").value(mockUrls.get(1)))
                .andDo(document("places-get-images",  // 문서 스니펫 이름 지정
                        pathParameters(
                                parameterWithName("placeId").description("조회할 장소 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("이미지 URL 리스트"),
                                fieldWithPath("data[]").description("이미지 URL 문자열")
                        )
                ));
    }

}