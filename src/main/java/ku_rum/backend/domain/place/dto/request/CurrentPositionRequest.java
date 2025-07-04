package ku_rum.backend.domain.place.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CurrentPositionRequest(
        @NotBlank(message = "위도 값은 필수 입니다.") BigDecimal latitude,
        @NotBlank(message = "경도 값은 필수 입니다.") BigDecimal longitude) {
}
