package ku_rum.backend.domain.building.domain.repository;

import ku_rum.backend.domain.building.domain.Building;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface BuildingViewRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByNumber(Integer number);

    @Query(value = """
    SELECT * FROM building
    WHERE name LIKE CONCAT('%', :searchTerm, '%')
       OR abbreviation LIKE CONCAT('%', :searchTerm, '%')
    """, nativeQuery = true)
    List<Building> searchByNameOrAbbreviation(@Param("searchTerm") String searchTerm);



}
