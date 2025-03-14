package ku_rum.backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(
        @NotBlank(message = "토큰은 비어있을 수 없습니다.") String refreshToken
) {
}
