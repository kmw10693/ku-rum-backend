package ku_rum.backend.domain.building.presentation;

import jakarta.validation.constraints.NotNull;
import ku_rum.backend.document.BuildingDocument;
import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.building.dto.request.BuildingInfoRequest;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.global.response.BaseResponse;
import ku_rum.backend.global.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/buildings/view")
public class BuildingAdminController {
  private final BuildingSearchService buildingSearchService;

  @PostMapping("/add")
  public BaseResponse<BuildingResponse> insertBuildingInfo(
          //@AuthenticationPrincipal CustomUserDetails userDetails,
          @RequestBody BuildingInfoRequest request){
    //userService.validateUserDetails(userDetails);
    BuildingResponse result = buildingSearchService.insertByAdminBuildingInfo(request);
    return BaseResponse.of(BaseExceptionResponseStatus.SUCCESS.getStatus(), result);
  }
}
