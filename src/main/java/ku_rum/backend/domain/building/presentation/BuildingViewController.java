package ku_rum.backend.domain.building.presentation;

import ku_rum.backend.domain.building.application.BuildingViewService;
import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class BuildingViewController {

    private final BuildingViewService buildingViewService;

    /**
     * 번호로 검색
     *
     * @param number
     * @return
     */
    @GetMapping("/search/{number}")
    public BaseResponse<BuildingViewResponse> getBuildingByNumber(@PathVariable("number") Integer number) {
        return BaseResponse.ok(buildingViewService.getBuildingByNumber(number));
    }

    /**
     * 이름 또는 약어로 검색 (ngram parser 적용 - mysql)
     *
     * @param name
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<BuildingViewResponse>> searchBuildings(
            @RequestParam(name = "name") String name
    ) {
        return BaseResponse.ok(buildingViewService.searchBuildings(name));
    }


}
