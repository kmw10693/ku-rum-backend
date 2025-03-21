package ku_rum.backend.domain.common.mail.dto.request;

import jakarta.validation.constraints.*;

public record MailVerificationRequest(

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 맞지 않습니다.")
        String email,

        @NotNull(message = "인증코드 입력은 필수입니다.")
        @Size(min = 6, max = 6)
        String code) {

    public MailVerificationRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
