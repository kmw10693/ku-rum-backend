package ku_rum.backend.domain.chatbot.application;

import ku_rum.backend.domain.chatbot.dto.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatbotService {
    private final RestClient restClient;

    public String chat(ChatRequest chatRequest) {
        Map<String, Object> requestBody = getModel(chatRequest.content());

        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

    private static Map<String, Object> getModel(String content) {
        return Map.of(
                "model", "gpt-4",
                "messages", List.of(Map.of("role", "user", "content", content))
        );
    }
}
