package ku_rum.backend.domain.building.application.method;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingQueryRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BuildingNameSearchStrategy implements SearchStrategy<Building>{
  private final BuildingQueryRepository buildingQueryRepository;

  public BuildingNameSearchStrategy(BuildingQueryRepository buildingQueryRepository) {
    this.buildingQueryRepository = buildingQueryRepository;
  }

  @Override
  public List<Building> search(String searchText) {
    return buildingQueryRepository.searchBuildingByNgram(searchText);
  }
}
