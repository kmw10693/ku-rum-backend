package ku_rum.backend.domain.chatbot.application;

import ku_rum.backend.domain.chatbot.dto.request.ChatRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ChatbotServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ChatbotService chatbotService;

    @Test
    @DisplayName("챗봇 요청 시 올바른 응답을 전달한다.")
    void chat_ShouldReturnResponse_WhenValidRequest() {
        // Given
        String userContent = "Hello";
        String expectedResponse = "test response";
        ChatRequest chatRequest = new ChatRequest(userContent);

        // RestClient 체이닝 모의
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(anyMap())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(expectedResponse);

        // When
        String actualResponse = chatbotService.chat(chatRequest);

        // Then
        assertEquals(expectedResponse, actualResponse);

        // RestClient 호출 검증
        ArgumentCaptor<Map<String, Object>> bodyCaptor = ArgumentCaptor.forClass(Map.class);
        verify(requestBodyUriSpec).body(bodyCaptor.capture());

        Map<String, Object> requestBody = bodyCaptor.getValue();
        assertEquals("gpt-4", requestBody.get("model"));
    }
}