package ku_rum.backend.domain.category.domain.repository;

import ku_rum.backend.domain.category.domain.BuildingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingCategoryRepository extends JpaRepository<BuildingCategory, Long> {
}
