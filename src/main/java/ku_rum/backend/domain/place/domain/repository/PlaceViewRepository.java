package ku_rum.backend.domain.place.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ku_rum.backend.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaceViewRepository extends JpaRepository<Place, Long> {

    @Query(value = "SELECT * FROM place p WHERE p.name LIKE %:categoryName%", nativeQuery = true)
    List<Place> findAllByCategory_Name(@Param("categoryName") String categoryName);
}