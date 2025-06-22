package ku_rum.backend.domain.category.domain.repository;

import ku_rum.backend.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT id FROM category WHERE name = :chipName", nativeQuery = true)
    List<Long> findIdsByChipName(@Param("chipName") String chipName);
}
