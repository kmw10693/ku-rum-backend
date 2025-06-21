package ku_rum.backend.domain.building.domain.repository;

import ku_rum.backend.domain.building.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findAll();
}
