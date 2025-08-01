package ku_rum.backend.domain.subPlace.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import ku_rum.backend.domain.subPlace.domain.CategoryChip;
import ku_rum.backend.domain.subPlace.domain.SubPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<SubPlace, Long> {

    @Query("""
                SELECT p
                FROM SubPlace p
                WHERE p.categoryChip = "BUILDING"
                ORDER BY
                    ABS(p.latitude - :latitude) + ABS(p.longitude - :longitude)
                LIMIT 1
            """)
    SubPlace findNearestPlace(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);

    Optional<SubPlace> findOneByName(String name);

    Optional<SubPlace> findByPlaceId(Long placeId);

    List<SubPlace> findByCategoryChip(CategoryChip categoryChip);

    List<SubPlace> findByNameContaining(String query);
}
