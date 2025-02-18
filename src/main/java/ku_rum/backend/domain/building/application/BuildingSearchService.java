package ku_rum.backend.domain.building.application;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.BuildingAbbrev;
import ku_rum.backend.domain.category.domain.repository.BuildingCategoryQueryRepository;
import ku_rum.backend.domain.building.domain.repository.BuildingQueryRepository;
import ku_rum.backend.domain.building.domain.repository.BuildingRepository;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.category.application.CategoryService;
import ku_rum.backend.domain.category.domain.Category;
import ku_rum.backend.domain.category.domain.CategoryDetail;
import ku_rum.backend.domain.category.domain.repository.CategoryRepository;
import ku_rum.backend.domain.category.dto.response.CategoryDetailResponse;
import ku_rum.backend.domain.menu.domain.repository.MenuRepository;
import ku_rum.backend.domain.menu.response.MenuSimpleResponse;
import ku_rum.backend.global.exception.building.BuildingNotFoundException;
import ku_rum.backend.global.exception.building.BuildingNotRegisteredException;
import ku_rum.backend.global.exception.category.CategoryNotExistException;
import ku_rum.backend.global.exception.category.CategoryNotProvidingDetailException;
import ku_rum.backend.global.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildingSearchService {
  private final BuildingQueryRepository buildingQueryRepository;
  private final BuildingRepository buildingRepository;
  private final CategoryService categoryService;
  private final BuildingCategoryQueryRepository buildingCategoryQueryRepository;
  private final MenuRepository menuQueryRepository;
  private final CategoryRepository categoryRepository;


  public List<BuildingResponse> findAllBuildings() {
    return Optional.ofNullable(buildingQueryRepository.findAllBuildings())
            .filter(buildings -> !buildings.isEmpty())
            .orElseThrow(() -> new BuildingNotRegisteredException(BaseExceptionResponseStatus.NO_BUILDING_REGISTERED_CURRENTLY));//리스트가 비어있는 경우 예외처리
  }

  public BuildingResponse viewBuildingByNumber(Long number) {
    return buildingQueryRepository.findBuildingByNumber(number)
            .orElseThrow(() -> new BuildingNotFoundException(BaseExceptionResponseStatus.BUILDING_DATA_NOT_FOUND_BY_NUMBER));
  }

  public Optional<BuildingResponse> viewBuildingByName(String name) {
    String finalName = removeNumbersInName(name);

    List<BuildingAbbrev> potentialMatches = Arrays.asList(BuildingAbbrev.values());

    BuildingAbbrev matchedBuilding = potentialMatches.stream()
            .filter(b -> b.getOriginalName().toLowerCase().equals(finalName) ||
                    b.name().toLowerCase().equals(finalName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Building name not found: " + name));

    return buildingQueryRepository.findBuildingByName(matchedBuilding.getOriginalName());
  }

  /**
   * 줄임말 이름인 경우, 숫자 제거 (ex. 공A101 -> 공A)
   *
   * @param name
   * @return
   */
  private String removeNumbersInName(String name) {

    return name.chars()
            .filter(ch -> !Character.isDigit(ch)) //문자만 남김
            .mapToObj(ch -> String.valueOf((char) ch)) //각 문자를 String으로
            .collect(Collectors.joining()); // 문자들을 하나의 문자열로 결합
  }

  /**
   * 유효한 줄임말 명칭인지 체크
   *
   * @param name
   */
  private BuildingAbbrev checkMatchWithAbbreviationName(String name) {
    return BuildingAbbrev.fromAbbrevName(name);
  }

  /**
   * 유효한 정식명칭인지 체크
   *
   * @param name
   */
  private BuildingAbbrev checkMatchWithOriginalName(String name) {
    return BuildingAbbrev.fromOriginalName(name);
  }

  public List<BuildingResponse> viewBuildingByCategory(String category) {
    List<Long> buildingIds = categoryService.findByCategoryReturnBuildingIds(category);//해당하는 기본키 리스트로 가져오기
    List<Building> buildingsFound = buildingQueryRepository.findAllByIdIn(buildingIds);
    return buildingsFound.stream()
            .map(building -> BuildingResponse.of(building))
            .collect(Collectors.toList());
  }


  public CategoryDetailResponse viewBuildingDetailByCategory(String category, Long buildingId) {
    if (validateDetailProvidingCategory(category))
    {
      CategoryDetail categoryDetail = getCategoryDetail(category);
      return getCategoryDetail(categoryDetail,buildingId);
    }else{
      throw new CategoryNotProvidingDetailException(BaseExceptionResponseStatus.CATEGORYNAME_NOT_PROVIDING_DETAIL);
    }
  }

  private CategoryDetailResponse<MenuSimpleResponse> getCategoryDetail(CategoryDetail categoryDetail, Long buildingId) {

    String category = categoryDetail.getCategoryName();
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new BuildingNotFoundException(BaseExceptionResponseStatus.BUILDING_DATA_NOT_FOUND_BY_NAME));
    Category categoryData = categoryRepository.findByName(category)
            .orElseThrow(() -> new CategoryNotExistException(BaseExceptionResponseStatus.CATEGORY_NAME_NOT_EXIST));

    // 필요한 정보들
    Long floor_info = building.getFloor();
    List<MenuSimpleResponse> menuList_info = new ArrayList<>();

    if (!category.equals(CategoryDetail.KCUBE.name())) { //KCUBE 카테고리가 아니면
      menuList_info = buildingCategoryQueryRepository.findByBuildingAndCategoryId(building.getId(), categoryData.getId())
              .map(buildingCategory -> menuQueryRepository.findAllByCategoryId(categoryData.getId()))
              .orElse(Collections.emptyList());

    }

    return CategoryDetailResponse.<MenuSimpleResponse>builder()
            .category(categoryDetail.getCategoryName())
            .floor(floor_info)
            .detailList(menuList_info)
            .build();

  }

  private CategoryDetail getCategoryDetail(String category) {
    if (category == null || category.isEmpty()) { // 입력값 검증
      return null;
    }
    return Arrays.stream(CategoryDetail.values())
            .filter(detail -> detail.getCategoryName().equals(category))
            .findFirst()
            .orElseThrow(() -> new CategoryNotProvidingDetailException(BaseExceptionResponseStatus.CATEGORYNAME_NOT_PROVIDING_DETAIL));
  }


  private boolean validateDetailProvidingCategory(String category) {
    for (CategoryDetail c : CategoryDetail.values()){
      if (category.trim().equals(c.getCategoryName()) || category.trim().equals(c.name())){
        return true;
      }
    }
    return false;
  }

  public List<BuildingResponse> searchAvailableText(String text) {

    // N-gram 인덱스를 활용
    String searchText = text.trim().toLowerCase();

    List<BuildingResponse> resultList = new ArrayList<>();

    // 빌딩 이름에서 매칭되는 부분 검색
    List<Building> buildingsFound = buildingQueryRepository.searchBuildingByNgram(searchText);
    List<BuildingResponse> buildingResponses = buildingsFound.stream()
            .map(building -> BuildingResponse.of(building))
            .collect(Collectors.toList());
    resultList.addAll(buildingResponses);

    // 카테고리 이름에서 매칭되는 부분 검색
    List<Category> categoriesFound = buildingCategoryQueryRepository.searchCategoryByNgram(searchText);
    for (Category category : categoriesFound) {
      List<Building> categoryBuildings = buildingQueryRepository.findAllByCategory(category);
      List<BuildingResponse> categoryBuildingResponses = categoryBuildings.stream()
              .map(building -> BuildingResponse.of(building))
              .collect(Collectors.toList());
      resultList.addAll(categoryBuildingResponses);
    }

    return resultList;
  }
}