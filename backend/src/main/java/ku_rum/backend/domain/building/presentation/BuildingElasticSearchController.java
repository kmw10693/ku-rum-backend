package ku_rum.backend.domain.building.presentation;

import jakarta.validation.constraints.NotNull;
import ku_rum.backend.document.BuildingDocument;
import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.response.BaseResponse;
import ku_rum.backend.global.response.status.BaseExceptionResponseStatus;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/buildings/view")
public class BuildingElasticSearchController {
  private final BuildingSearchService buildingSearchService;
  private final UserService userService;

  /**
   * 빌딩 명칭 글자 단위로 검색
   *
   * @return
   */
  @GetMapping("/searchName/token")
  public BaseResponse<List<BuildingDocument>> viewBuildingByNameToken(
          //@AuthenticationPrincipal CustomUserDetails userDetails,
          @RequestParam("name")@NotNull String name){
    //userService.validateUserDetails(userDetails);
    List<BuildingDocument> result = buildingSearchService.searchByBuildingnameToken(name.trim());
    return BaseResponse.okList(result);
  }

  /**
   * 성능 비교를 위한 임시 함수 (삭제 예정)
   *
   * @param name
   * @return
   */
  @GetMapping("/searchName/fulltext")
  public BaseResponse<List<BuildingResponse>> viewBuildingByNameFullText(
          //@AuthenticationPrincipal CustomUserDetails userDetails,
          @RequestParam("name")@NotNull String name){
    //userService.validateUserDetails(userDetails);
    List<BuildingResponse> result = buildingSearchService.searchByBuildingnameFullText(name.trim());
    return BaseResponse.okList(result);
  }




}
