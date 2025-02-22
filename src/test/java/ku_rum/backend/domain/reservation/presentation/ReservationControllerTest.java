package ku_rum.backend.domain.reservation.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.reservation.application.ReservationService;
import ku_rum.backend.domain.reservation.dto.request.SelectDateRequest;
import ku_rum.backend.domain.reservation.dto.request.WeinLoginRequest;
import ku_rum.backend.global.support.response.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
class ReservationControllerTest extends RestDocsTestSupport {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("크롤링 시작 요청 성공")
    @Test
    @WithMockUser
    void crawlReservationPageSuccess() throws Exception {
        // Mock 데이터 설정
        WeinLoginRequest loginRequest = new WeinLoginRequest("mockUser", "mockPassword");

        when(reservationService.crawlReservationPage(any(WeinLoginRequest.class)))
                .thenReturn(BaseResponse.of(HttpStatus.OK, "로그인 및 크롤링 작업 성공"));

        // 요청 및 검증
        mockMvc.perform(post("/api/v1/reservations/crawl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("예약 API")
                                        .description("건국대학교 위인전 로그인")
                                        .requestFields(
                                                fieldWithPath("userId")
                                                        .type(JsonType.STRING)
                                                        .description("건국대학교 위인전 아이디"),
                                                fieldWithPath("password")
                                                        .type(JsonType.STRING)
                                                        .description("건국대학교 위인전 비밀번호")
                                        )
                                        .responseFields(
                                                fieldWithPath("code")
                                                        .type(JsonType.NUMBER)
                                                        .description("성공시 반환 코드 (200)"),
                                                fieldWithPath("status")
                                                        .type(JsonType.STRING)
                                                        .description("성공시 상태 값 (OK)"),
                                                fieldWithPath("message")
                                                        .type(JsonType.STRING)
                                                        .description("성공 시 메시지 값 (OK)"),
                                                fieldWithPath("data")
                                                        .type(JsonType.STRING)
                                                        .description("성공 시 '로그인 및 크롤링 작업 성공' 반환")
                                        ).build())));
    }

    @DisplayName("날짜 선택 요청 성공")
    @Test
    @WithMockUser
    void selectDateSuccess() throws Exception {
        // Mock 데이터 설정
        SelectDateRequest selectDateRequest = new SelectDateRequest("2024-12-11");

        when(reservationService.selectDateAndFetchTable(anyString()))
                .thenReturn(BaseResponse.of(HttpStatus.OK, "타임 테이블 데이터"));

        // 요청 및 검증
        mockMvc.perform(post("/api/v1/reservations/select_date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectDateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("예약 API")
                                        .description("날짜 선택")
                                        .requestFields(
                                                fieldWithPath("selectedDate")
                                                        .type(JsonType.STRING)
                                                        .description("선택된 날짜")
                                        )
                                        .responseFields(
                                                fieldWithPath("code")
                                                        .type(JsonType.NUMBER)
                                                        .description("성공시 반환 코드 (200)"),
                                                fieldWithPath("status")
                                                        .type(JsonType.STRING)
                                                        .description("성공시 상태 값 (OK)"),
                                                fieldWithPath("message")
                                                        .type(JsonType.STRING)
                                                        .description("성공 시 메시지 값 (OK)"),
                                                fieldWithPath("data")
                                                        .type(JsonType.STRING)
                                                        .description("성공 시 '타임 테이블 데이터' 반환")
                                        ).build())));
    }
}