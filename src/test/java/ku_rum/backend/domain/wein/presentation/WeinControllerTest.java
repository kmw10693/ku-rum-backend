package ku_rum.backend.domain.wein.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.reservation.dto.response.WeinLoginResponse;
import ku_rum.backend.domain.wein.application.WeinService;
import ku_rum.backend.domain.wein.dto.request.WeinLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class WeinControllerTest extends RestDocsTestSupport {

    @MockBean
    private WeinService weinService;

    @DisplayName("위인전 로그인 API")
    @Test
    @WithMockUser
    void loginToWein() throws Exception {
        //given
        WeinLoginRequest weinLoginRequest = new WeinLoginRequest("testtest123", "password123");
        //when
        mockMvc.perform(post("/api/v1/wein/login")
                        .content(objectMapper.writeValueAsString(weinLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("위인전 로그인에 성공하였습니다"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                        .tag("위인전 API")
                                                .description("위인전 로그인")
                                .requestFields(
                                fieldWithPath("userId")
                                        .type(JsonType.STRING)
                                        .description("멤버 아이디")
                                        .attributes(constraints("아이디 입력은 필수입니다.")),
                                fieldWithPath("password")
                                        .type(JsonType.STRING)
                                        .description("비밀번호")
                                        .attributes(constraints("비밀번호 입력은 필수입니다."))
                        )
                                .responseFields(
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
                                        .description("성공 시 데이터")
                        ).build())));
    }

}