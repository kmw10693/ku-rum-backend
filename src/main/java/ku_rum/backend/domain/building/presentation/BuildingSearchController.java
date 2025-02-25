package ku_rum.backend.domain.building.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.category.dto.request.BuildindgCategoryRequest;
import ku_rum.backend.domain.category.dto.response.CategoryDetailResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.response.BaseResponse;
import ku_rum.backend.global.response.status.BaseExceptionResponseStatus;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/building/search")
public class BuildingSearchController {

  private final BuildingSearchService buildingSearchService;
  private final UserService userService;

  /**
   * 작성자: 이혜리
   * 수정일자: 2025-02-22
   * 모든 빌딩의 정보 반환
   * @return
   */
  @GetMapping
  public BaseResponse<List<BuildingResponse>> viewAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.validateUserDetails(userDetails);
    List<BuildingResponse> resultList = buildingSearchService.findAllBuildings();
    return BaseResponse.ok(resultList);
  }

  /**
   * 작성자: 이혜리
   * 수정일자: 2025-02-22
   * 건물번호로 빌딩의 정보 반환
   * @param number
   * @return
   */
  @GetMapping("/number")
  public BaseResponse<BuildingResponse> viewBuildingByNumber(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestParam("number")@NotNull @Min(1)  Long number)
  {
    userService.validateUserDetails(userDetails);
    BuildingResponse result = buildingSearchService.viewBuildingByNumber(number);
    return BaseResponse.ok(result);
  }

  /**
   * 작성자: 이혜리
   * 수정일자: 2025-02-23
   * 건물정보(건물이름, 줄임말)로 빌딩의 정보 반환 - full text 검색 부분과 비교하기 위함
   * @param name
   * @return
   */
  @GetMapping("/name")
  public BaseResponse<BuildingResponse> viewBuildingByName(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestParam("name")@NotNull String name)
  {
    userService.validateUserDetails(userDetails);
    BuildingResponse result = buildingSearchService.viewBuildingByName(name.trim());
    return BaseResponse.ok(result);
  }

  /**
   * 작성자: 이혜리
   * 수정일자: 2025-02-23
   * 카테고리 버튼으로 빌딩의 정보 반환
   * @param category
   * @return
   */
  @GetMapping("/category")
  public BaseResponse<List<BuildingResponse>> viewBuildingByCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathParam("category") String category){
    userService.validateUserDetails(userDetails);
    List<BuildingResponse> resultList = buildingSearchService.viewBuildingByCategory(category.trim());
    return BaseResponse.ok(resultList);
  }

  /**
   * 작성자: 이혜리
   * 수정일자: 2025-02-25
   * full text 검색으로 건물명,카테고리명으로 빌딩의 정보 반환
   * @param userDetails
   * @param text
   * @return
   */
  @GetMapping("/text")
  public BaseResponse<List<BuildingResponse>> viewAvailableTextNameList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathParam("text") String text)
  {
    userService.validateUserDetails(userDetails);
    List<BuildingResponse> resultList = buildingSearchService.searchAvailableText(text);
    return BaseResponse.ok(resultList);
  }

  /**
   * 작성자: 이혜리
   * 수정일자: 2025-02-25
   * 카테고리에 해당하는 특정 핀포인트 디테일 정보 확인 (학생식당, K-CUBE/K-HUB)
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

}