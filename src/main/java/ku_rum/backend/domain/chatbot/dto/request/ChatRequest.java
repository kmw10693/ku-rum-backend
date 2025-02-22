package ku_rum.backend.domain.chatbot.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String content) {
}
