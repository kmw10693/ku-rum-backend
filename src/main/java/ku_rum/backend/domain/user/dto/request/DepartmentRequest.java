package ku_rum.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record DepartmentRequest(
        @NotNull String department
) {
}
