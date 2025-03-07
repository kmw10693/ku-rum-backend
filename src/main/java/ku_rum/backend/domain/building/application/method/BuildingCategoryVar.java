package ku_rum.backend.domain.building.application.method;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.category.domain.Category;

public record BuildingCategoryVar(
        Building building,
        Category categoryData) {
}
