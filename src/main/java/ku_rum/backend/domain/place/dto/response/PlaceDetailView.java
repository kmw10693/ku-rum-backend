package ku_rum.backend.domain.place.dto.response;


import java.math.BigDecimal;

public record PlaceDetailView(
        Long id,
        String name,
        String subName,
        String text,
        BigDecimal latitude,
        BigDecimal longitude,
        Long buildingId,
        String buildingName,
        Long categoryId,
        String categoryName
) {
}
