package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.domain.place.application.PlaceFriendInfoService;
import ku_rum.backend.domain.place.dto.request.DeleteSearchTermRequest;
import ku_rum.backend.domain.place.dto.request.PlaceSearchRequest;
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
     * @param request
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<List<PlaceSearchInfoResponse>> getPlacesNameBySearch(
            @RequestBody PlaceSearchRequest request
    ) {
        List<PlaceSearchInfoResponse> response = placeFriendInfoService.getPlacesNameBySearch(request.search());
        return BaseResponse.ok(response);
    }



    /**
     * 8. 건물명, 강의실명, 건물번호 검색 디테일 반환
     *
     * @param request
     * @return
     */
    @PostMapping("/search/detail")
    public BaseResponse<List<PlaceFriendInfoResponse>> getDetailWithPlacesNameBySearch(
            @RequestBody PlaceSearchRequest request
    ) {
        List<PlaceFriendInfoResponse> response = placeFriendInfoService.getDetailWithPlacesNameBySearch(request.search());
        return BaseResponse.ok(response);
    }

    /**
     * 9. 건물명, 강의실명, 건물번호 검색어 반환
     *
     * @return
     */
    @GetMapping("/search/term")
    public BaseResponse<List<String>> getSearchTermList() {
        List<String> response = placeFriendInfoService.getSearchTermList();
        return BaseResponse.ok(response);
    }

    /**
     * 10. 건물명, 강의실명, 건물번호 검색어 삭제
     *
     * @param request
     * @return
     */
    @DeleteMapping("/search/term")
    public BaseResponse<String> deleteSearchTerm(@RequestBody DeleteSearchTermRequest request) {
        String result = placeFriendInfoService.deleteSearchTerm(request.term());
        return BaseResponse.ok(result);
    }


}
