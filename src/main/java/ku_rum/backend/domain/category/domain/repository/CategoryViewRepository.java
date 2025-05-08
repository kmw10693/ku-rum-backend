package ku_rum.backend.domain.category.domain.repository;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryViewRepository extends JpaRepository<Category, Long> {

}