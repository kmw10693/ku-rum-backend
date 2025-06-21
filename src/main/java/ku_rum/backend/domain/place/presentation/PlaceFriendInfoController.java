package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.domain.place.application.PlaceFriendInfoService;
import ku_rum.backend.domain.place.dto.response.PlaceFriendInfo2Response;
import ku_rum.backend.domain.place.dto.response.PlaceFriendInfoResponse;
import ku_rum.backend.domain.place.dto.response.PlaceSearchInfoResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
public class PlaceFriendInfoController {

    private final PlaceFriendInfoService placeFriendInfoService;

    /**
     * 5. 칩별 제목, 부제목, 정보 반환 (친구 칩 제외)
     *
     * @param chipName
     * @return
     */
    @PostMapping("/chip/{chipName}")
    public BaseResponse<List<PlaceFriendInfoResponse>> getChipDetailInfo(@PathVariable("chipName") String chipName) {
        List<PlaceFriendInfoResponse> response = placeFriendInfoService.getPlaceFriendInfoList(chipName);
        return BaseResponse.ok(response);
    }

    /**
     * 6. 칩별 제목, 부제목, 정보 반환 (친구 칩)
     *
     * @return
     */
    @PostMapping("/chip/friends")
    public BaseResponse<List<PlaceFriendInfo2Response>> getFriendsChipDetailInfo() {
        List<PlaceFriendInfo2Response> response = placeFriendInfoService.getPlaceFriendInfoList2();
        return BaseResponse.ok(response);
    }

    /**
     * 7. 건물명, 강의실명, 건물번호 검색 여러개(제목) 반환
     *
     * @param search
     * @return
     */
    @PostMapping
    public BaseResponse<List<PlaceSearchInfoResponse>> getPlacesNameBySearch(
            @RequestParam("search") String search
    ) {
        List<PlaceSearchInfoResponse> response = placeFriendInfoService.getPlacesNameBySearch(search);
        return BaseResponse.ok(response);
    }


    /**
     * 8. 건물명, 강의실명, 건물번호 검색 디테일 반환
     *
     * @param search
     * @return
     */
    @PostMapping("/search/detail")
    public BaseResponse<List<PlaceFriendInfoResponse>> getDetailWithPlacesNameBySearch(
            @RequestParam("search") String search
    ) {
        List<PlaceFriendInfoResponse> response = placeFriendInfoService.getDetailWithPlacesNameBySearch(search);
        return BaseResponse.ok(response);
    }


}
