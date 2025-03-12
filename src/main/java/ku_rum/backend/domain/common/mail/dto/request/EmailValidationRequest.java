package ku_rum.backend.domain.common.mail.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailValidationRequest(
        @NotBlank String email
) {
}
