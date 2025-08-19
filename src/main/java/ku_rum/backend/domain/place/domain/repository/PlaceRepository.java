package ku_rum.backend.domain.place.domain.repository;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import ku_rum.backend.domain.place.domain.CategoryChip;
import ku_rum.backend.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByNameContaining(@NotNull String name);

    List<Place> findByCategoryChip(CategoryChip categoryChip);
}
