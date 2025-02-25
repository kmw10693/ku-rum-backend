package ku_rum.backend.domain.chatbot.presentation;

import ku_rum.backend.domain.chatbot.application.ChatbotService;
import ku_rum.backend.domain.chatbot.dto.request.ChatRequest;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public BaseResponse<String> chat(@RequestBody ChatRequest chatRequest) {
        return BaseResponse.ok(chatbotService.chat(chatRequest));
    }
}
