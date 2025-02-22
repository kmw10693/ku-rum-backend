package ku_rum.backend.domain.category.application;

import ku_rum.backend.domain.category.domain.repository.BuildingCategoryQueryRepository;
import ku_rum.backend.domain.category.domain.repository.CategoryQueryRepository;
import ku_rum.backend.domain.category.domain.repository.CategoryRepository;
import ku_rum.backend.global.exception.category.CategoryNotExistException;
import ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final BuildingCategoryQueryRepository buildingCategoryQueryRepository;
  private final CategoryQueryRepository categoryQueryRepository;

  public List<Long> findByCategoryReturnBuildingIds(String category){
    List<Long> categoryIds = categoryQueryRepository.findAllByName(category)
            .orElseThrow(() -> new CategoryNotExistException(BaseExceptionResponseStatus.CATEGORY_NAME_NOT_EXIST));
    List<Long> buildingCategoryIds = buildingCategoryQueryRepository.findBuildingCategoryIds(categoryIds);
    return buildingCategoryQueryRepository.findBuildingIdsByCategoryIds(buildingCategoryIds);
  }
}