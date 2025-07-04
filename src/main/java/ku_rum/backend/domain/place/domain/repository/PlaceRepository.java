package ku_rum.backend.domain.place.domain.repository;

import java.math.BigDecimal;
import java.util.Optional;
import ku_rum.backend.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place,Long> {

    @Query("""
        SELECT p
        FROM Place p
        WHERE p.category = "BUILDING"
        ORDER BY
            POWER(p.latitude - :latitude, 2) + POWER(p.longitude - :longitude, 2)
        LIMIT 1
    """)
    Place findNearestPlace(@Param("latitude") BigDecimal latitude, @Param("longitude")BigDecimal longitude);

    Optional<Place> findOneByName(String name);
}
