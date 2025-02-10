package ku_rum.backend.domain.buildingCategory.dto.request;

import lombok.Builder;

@Builder
public record BuildindgCategoryRequest(
        String category,
        Long buildingId
) {
}
