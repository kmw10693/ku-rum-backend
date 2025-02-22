package ku_rum.backend.domain.building.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.category.dto.request.BuildindgCategoryRequest;
import ku_rum.backend.domain.category.dto.response.CategoryDetailResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.support.response.BaseResponse;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/buildings/view")
public class BuildingSearchController {

  private final BuildingSearchService buildingSearchService;
  private final UserService userService;

  /**
   * 전체 강의실의 핀포인트 조회
   *
   * @return
   */
  @GetMapping
  public BaseResponse viewAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.validateUserDetails(userDetails);
    List<BuildingResponse> results = buildingSearchService.findAllBuildings();
    return BaseResponse.of(SUCCESS.getStatus(),results);
  }

  /**
   * 특정 강의실의 핀포인트 조회 (건물번호로)
   *
   * @param number
   * @return
   */
  @GetMapping("/searchNumber")
  public BaseResponse<BuildingResponse> viewBuildingByNumber(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("number")@NotNull @Min(1)  Long number) {
    userService.validateUserDetails(userDetails);
    BuildingResponse result = buildingSearchService.viewBuildingByNumber(number);
    return BaseResponse.of(SUCCESS.getStatus(), result);
  }

  /**
   * 특정 강의실의 핀포인트 조회 (건물정식명칭으로, 건물 줄임말로)
   *
   * @param name
   * @return
   */
  @GetMapping("/searchName")
  public BaseResponse<Optional<BuildingResponse>> viewBuildingByName(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("name")@NotNull String name){
    userService.validateUserDetails(userDetails);
    Optional<BuildingResponse> result = buildingSearchService.viewBuildingByName(name.trim());
    return BaseResponse.of(SUCCESS.getStatus(), result);
  }

  /**
   * 특정 카테고리의 핀포인트들 조회 (카테고리명으로)
   *
   * @param category
   * @return
   */
  @GetMapping("/category/{category}")
  public BaseResponse<List> viewBuildingByCategory(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("category") String category){
    userService.validateUserDetails(userDetails);
    List<BuildingResponse> categoryList = buildingSearchService.viewBuildingByCategory(category.trim());
    return BaseResponse.of(SUCCESS.getStatus(), categoryList);
  }

  /**
   * 카테고리에 해당하는 특정 핀포인트 디테일 정보 확인 (학생식당, K-CUBE/K-HUB)
   *
   * @param userDetails
   * @param request
   * @return
   */
  @PostMapping
  public BaseResponse<CategoryDetailResponse> viewBuildingByCategoryInBuilding(
          @AuthenticationPrincipal CustomUserDetails userDetails,
          @RequestBody BuildindgCategoryRequest  request
  ){
    userService.validateUserDetails(userDetails);
    CategoryDetailResponse categoryDetailResponse = buildingSearchService.viewBuildingDetailByCategory(request.category(), request.buildingId());
    return BaseResponse.of(SUCCESS.getStatus(), categoryDetailResponse);
  }

  /**
   * full text 검색을 위한 함수
   *
   * @param userDetails
   * @param text
   * @return
   */
  @GetMapping("/text/{text}")
  public BaseResponse<List<BuildingResponse>> viewAvailableTextNameList(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("text") String text){
    userService.validateUserDetails(userDetails);
    List<BuildingResponse> resultList = buildingSearchService.searchAvailableText(text);
    return BaseResponse.of(SUCCESS.getStatus(), resultList);
  }

}