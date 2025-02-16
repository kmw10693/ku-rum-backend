package ku_rum.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProfileChangeRequest
        (@NotNull String imageUrl) {
}
