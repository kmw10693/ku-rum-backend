package ku_rum.backend.domain.building.repository;

import ku_rum.backend.domain.building.domain.Building;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface BuildingViewRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByNumber(Integer number);

    @Query(value = """
    SELECT * FROM building
    WHERE
        (
            (:name IS NOT NULL AND MATCH(name) AGAINST (:name IN BOOLEAN MODE))
            OR
            (:abbreviation IS NOT NULL AND MATCH(abbreviation) AGAINST (:abbreviation IN BOOLEAN MODE))
        )
    """, nativeQuery = true)
    List<Building> searchByNameOrAbbreviation(@Param("name") String name, @Param("abbreviation") String abbreviation);



}
