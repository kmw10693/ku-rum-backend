package ku_rum.backend.domain.building.domain.repository;

import ku_rum.backend.domain.building.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findAll();

    @Query("""
    SELECT b FROM Building b
    WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(b.abbreviation) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Building> findBuildingNamesByKeyword(@Param("keyword") String keyword);

}
