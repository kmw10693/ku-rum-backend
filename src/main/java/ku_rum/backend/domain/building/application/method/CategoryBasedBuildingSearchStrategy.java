package ku_rum.backend.domain.building.application.method;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingQueryRepository;
import ku_rum.backend.domain.category.domain.Category;
import ku_rum.backend.domain.category.domain.repository.BuildingCategoryQueryRepository;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CategoryBasedBuildingSearchStrategy implements SearchStrategy<Building>{
  private final BuildingQueryRepository buildingQueryRepository;
  private final BuildingCategoryQueryRepository buildingCategoryQueryRepository;

  public CategoryBasedBuildingSearchStrategy(BuildingQueryRepository buildingQueryRepository, BuildingCategoryQueryRepository buildingCategoryQueryRepository) {
    this.buildingQueryRepository = buildingQueryRepository;
    this.buildingCategoryQueryRepository = buildingCategoryQueryRepository;
  }

  @Override
  public List<Building> search(String searchText) {
    List<Category> categoryList = buildingCategoryQueryRepository.searchCategoryByNgram(searchText);
    if (categoryList == null || categoryList.isEmpty()) {
      return Collections.emptyList();
    }
    return categoryList.stream()
            .map(buildingQueryRepository::searchBuildingByCategoryNgram)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .collect(Collectors.toList());
  }
}
