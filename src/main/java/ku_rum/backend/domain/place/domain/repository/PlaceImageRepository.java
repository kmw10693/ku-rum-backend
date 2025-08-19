package ku_rum.backend.domain.place.domain.repository;

import java.util.List;
import ku_rum.backend.domain.place.domain.PlaceImage;
import ku_rum.backend.domain.place.domain.SubPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {
    @Query("SELECT pi FROM PlaceImage pi WHERE pi.subPlace IN :places")
    List<PlaceImage> findBySubPlaceIn(@Param("places") List<SubPlace> subPlaces);

    List<PlaceImage> findBySubPlace(SubPlace subPlace);
}
