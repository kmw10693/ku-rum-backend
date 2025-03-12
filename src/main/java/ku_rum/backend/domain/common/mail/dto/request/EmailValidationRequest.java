package ku_rum.backend.domain.common.mail.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmailValidationRequest(
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email
) {
    public EmailValidationRequest(String email) {
        this.email = email;
    }
}
