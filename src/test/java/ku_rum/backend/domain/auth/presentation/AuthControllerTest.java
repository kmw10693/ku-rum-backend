package ku_rum.backend.domain.auth.presentation;

import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.auth.application.AuthService;
import ku_rum.backend.domain.auth.dto.request.LoginRequest;
import ku_rum.backend.domain.auth.dto.request.ReissueRequest;
import ku_rum.backend.global.security.jwt.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class AuthControllerTest extends RestDocsTestSupport {

    @MockBean
    private AuthService authService;

    @DisplayName("로그인을 진행한다.")
    @Test
    @WithMockUser
    void login() throws Exception {
        //given
        LoginRequest request = new LoginRequest("kmw10693", "testtest");
        TokenResponse tokenResponse = TokenResponse.of("accessToken", "refreshToken", 1800000L, 604800000L);
        Mockito.when(authService.login(request)).thenReturn(tokenResponse);

        // when then
        mockMvc.perform(post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.accessToken").value("accessToken"))
                .andExpect(jsonPath("data.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("data.accessExpireIn").value(1800000L))
                .andExpect(jsonPath("data.refreshExpireIn").value(604800000L))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("loginId")
                                        .type(JsonType.STRING)
                                        .description("멤버 아이디")
                                        .attributes(constraints("아이디 입력은 필수입니다. 최소 6자 이상입니다.")),
                                fieldWithPath("password")
                                        .type(JsonType.STRING)
                                        .description("비밀번호")
                                        .attributes(constraints("비밀번호 입력은 필수입니다,"))
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.STRING)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 (OK)"),
                                fieldWithPath("data.accessToken")
                                        .type(JsonType.STRING)
                                        .description("엑세스 토큰"),
                                fieldWithPath("data.refreshToken")
                                        .type(JsonType.STRING)
                                        .description("리프레시 토큰"),
                                fieldWithPath("data.accessExpireIn")
                                        .type(JsonType.STRING)
                                        .description("엑세스 토큰 만료 기간"),
                                fieldWithPath("data.refreshExpireIn")
                                        .type(JsonType.STRING)
                                        .description("리프레시 토큰 만료 기간")
                        )));
    }

    @DisplayName("로그아웃을 진행한다.")
    @Test
    @WithMockUser
    void logout() throws Exception {
        // when then
        mockMvc.perform(patch("/api/v1/auth/logout")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.STRING)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 (OK)"),
                                fieldWithPath("data")
                                        .type(JsonType.STRING)
                                        .description("성공 시 반환 메시지")
                        )));
    }

    @DisplayName("토큰을 재발급한다")
    @Test
    @WithMockUser
    void reissue() throws Exception {
        ReissueRequest request = new ReissueRequest("refreshToken");
        TokenResponse tokenResponse = TokenResponse.of("accessToken", "refreshToken", 1800000L, 604800000L);
        Mockito.when(authService.reissue(request)).thenReturn(tokenResponse);

        // when then
        mockMvc.perform(patch("/api/v1/auth/reissue")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("refreshToken")
                                        .type(JsonType.STRING)
                                        .description("발급받은 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.STRING)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 (OK)"),
                                fieldWithPath("data.accessToken")
                                        .type(JsonType.STRING)
                                        .description("엑세스 토큰"),
                                fieldWithPath("data.refreshToken")
                                        .type(JsonType.STRING)
                                        .description("리프레시 토큰"),
                                fieldWithPath("data.accessExpireIn")
                                        .type(JsonType.STRING)
                                        .description("엑세스 토큰 만료 기간"),
                                fieldWithPath("data.refreshExpireIn")
                                        .type(JsonType.STRING)
                                        .description("리프레시 토큰 만료 기간")
                        )));
    }
}