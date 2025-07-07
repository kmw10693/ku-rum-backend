package ku_rum.backend.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CurrentPositionRequest(
        @NotNull(message = "위도 값은 필수 입니다.") BigDecimal latitude,
        @NotNull(message = "경도 값은 필수 입니다.") BigDecimal longitude) {
}
