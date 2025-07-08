package ku_rum.backend.domain.place.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CurrentPositionConfirmRequest(
        @NotBlank(message = "장소 이름은 필수 입니다.") String placeName) {
}
