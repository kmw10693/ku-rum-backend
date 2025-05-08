package ku_rum.backend.domain.place.domain.repository;

import ku_rum.backend.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaceViewRepository extends JpaRepository<Place, Long> {
    List<Place> findAllByCategory_Name(String categoryName);
}