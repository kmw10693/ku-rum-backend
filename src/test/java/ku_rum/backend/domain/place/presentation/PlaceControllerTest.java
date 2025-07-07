package ku_rum.backend.domain.place.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.place.application.PositionService;
import ku_rum.backend.domain.place.dto.request.CurrentPositionConfirmRequest;
import ku_rum.backend.domain.place.dto.request.CurrentPositionRequest;
import ku_rum.backend.domain.place.dto.response.CurrentPositionConfirmResponse;
import ku_rum.backend.domain.place.dto.response.CurrentPositionResponse;
import ku_rum.backend.domain.place.dto.response.CurrentPositionStatusResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PlaceControllerTest extends RestDocsTestSupport {

    @MockBean
    PositionService positionService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .loginId("testuser")
                .email("test@example.com")
                .nickname("테스트용")
                .build();
        CustomUserDetails userDetails = CustomUserDetails.from(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
    }

    @DisplayName("현재 유저의 위치 공유여부를 확인한다.")
    @Test
    void getCurrentPositionStatus() throws Exception {
        //given
        CurrentPositionStatusResponse response = new CurrentPositionStatusResponse(Boolean.FALSE);
        given(positionService.getCurrentPositionStatus(any(CustomUserDetails.class)))
                .willReturn(response);

        //when
        mockMvc.perform(get("/api/v1/places/sharing/status")
                        .header("Authorization",
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpㄴGJdOigSKjxMIab0cV06xFjSpwrq70"))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.isActive").value(false))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("지도 관련 API")
                                .description("위치 공유 상태 조회")
                                .requestHeaders(
                                        headerWithName("Authorization").description("발급 받은 엑세스 토큰입니다.")
                                )
                                .build())));
    }

    @DisplayName("현재 유저의 위치에 해당하는 공유 건물을 조회한다")
    @Test
    void getCurrentPosition() throws Exception {
        //given
        String placeName = "상허기념도서관";
        CurrentPositionRequest request = new CurrentPositionRequest(BigDecimal.valueOf(37.53712),
                BigDecimal.valueOf(127.085795));
        CurrentPositionResponse response = new CurrentPositionResponse(placeName);

        given(positionService.getCurrentPosition(any(CustomUserDetails.class), eq(request)))
                .willReturn(response);

        //when
        mockMvc.perform(post("/api/v1/places/sharing")
                        .header("Authorization",
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpㄴGJdOigSKjxMIab0cV06xFjSpwrq70")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.placeName").value(placeName))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("지도 관련 API")
                                .description("위치 공유값 조회")
                                .requestHeaders(
                                        headerWithName("Authorization").description("발급 받은 엑세스 토큰입니다.")
                                )
                                .build())));
    }

    @DisplayName("현재 유저의 위치를 공유한다")
    @Test
    void sharingPosition() throws Exception {
        //given
        String placeName = "상허기념도서관";
        CurrentPositionConfirmRequest request = new CurrentPositionConfirmRequest(placeName);
        CurrentPositionConfirmResponse response = new CurrentPositionConfirmResponse(placeName);

        given(positionService.confirmCurrentPosition(any(CustomUserDetails.class), eq(request)))
                .willReturn(response);

        //when
        mockMvc.perform(post("/api/v1/places/sharing/confirm")
                        .header("Authorization",
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpㄴGJdOigSKjxMIab0cV06xFjSpwrq70")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.placeName").value(placeName))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("지도 관련 API")
                                .description("위치 공유")
                                .requestHeaders(
                                        headerWithName("Authorization").description("발급 받은 엑세스 토큰입니다.")
                                )
                                .build())));
    }

    @DisplayName("현재 유저의 위치를 공유한다")
    @Test
    void disableSharingPosition() throws Exception {
        //given
        doNothing().when(positionService).disableSharingPosition(any(CustomUserDetails.class));

        //when
        mockMvc.perform(delete("/api/v1/places/sharing/confirm")
                        .header("Authorization",
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpㄴGJdOigSKjxMIab0cV06xFjSpwrq70"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("지도 관련 API")
                                .description("위치 공유 중지")
                                .requestHeaders(
                                        headerWithName("Authorization").description("발급 받은 엑세스 토큰입니다.")
                                )
                                .build())));
    }
}