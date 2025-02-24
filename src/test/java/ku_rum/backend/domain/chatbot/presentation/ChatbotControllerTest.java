package ku_rum.backend.domain.chatbot.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.chatbot.application.ChatbotService;
import ku_rum.backend.domain.chatbot.dto.request.ChatRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ChatbotControllerTest extends RestDocsTestSupport {

    @MockBean
    private ChatbotService chatbotService;

    @DisplayName("챗봇과 대화를 수행한다.")
    @Test
    @WithMockUser
    void chat() throws Exception {
        // given
        ChatRequest chatRequest = new ChatRequest("안녕하세요, 챗봇!");
        given(chatbotService.chat(any(ChatRequest.class))).willReturn("안녕하세요! 무엇을 도와드릴까요?");

        // when & then
        mockMvc.perform(post("/api/v1/chatbot/chat")
                        .content(objectMapper.writeValueAsString(chatRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("안녕하세요! 무엇을 도와드릴까요?"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("챗봇 API")
                                .description("챗봇과 대화 요청")
                                .requestFields(
                                        fieldWithPath("content")
                                                .type(JsonType.STRING)
                                                .description("사용자가 챗봇에게 보내는 메시지")
                                                .attributes(constraints("빈 값일 수 없습니다. 최소 1자 이상 입력하세요."))
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
                                                .description("챗봇의 응답 메시지")
                                ).build())));
    }
}