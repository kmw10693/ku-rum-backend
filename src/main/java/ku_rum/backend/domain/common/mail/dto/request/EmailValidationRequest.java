package ku_rum.backend.domain.common.mail.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailValidationRequest(
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 맞지 않습니다.")
        @NotBlank(message = "공백일 수 없습니다.")
        String email
) {
    public EmailValidationRequest(String email) {
        this.email = email;
    }
}
