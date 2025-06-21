package ku_rum.backend.domain.place.domain.repository;

import ku_rum.backend.domain.category.domain.Category;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.dto.response.PlaceSearchInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(value = "SELECT * FROM place WHERE category_id IN (:categoryIds)", nativeQuery = true)
    List<Place> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    @Query("SELECT p FROM Place p WHERE p.name = :activeBuildingName")
    List<Place> findPlacesByActiveBuildingName(@Param("activeBuildingName") String activeBuildingName);

    @Query("""
    SELECT new ku_rum.backend.domain.place.dto.response.PlaceSearchInfoResponse(p.name)
    FROM Place p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.subName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<PlaceSearchInfoResponse> searchPlacesByName(@Param("keyword") String keyword);

    List<Place> findByBuildingNameIn(List<String> buildingNames);

    @Query(value = "SELECT * FROM place WHERE name LIKE %:search% OR subName LIKE %:search%", nativeQuery = true)
    List<Place> findByNameOrSubNameContaining(@Param("search") String search);

    @Query(value = "SELECT * FROM place WHERE building_id = :id", nativeQuery = true)
    List<Place> findByBuildingId(@Param("id") Long id);

}
