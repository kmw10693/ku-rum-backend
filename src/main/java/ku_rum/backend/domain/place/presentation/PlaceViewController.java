package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.domain.place.application.PlaceViewService;
import ku_rum.backend.domain.place.dto.response.PlaceDetailView;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceViewController {

    private final PlaceViewService placeViewService;

    /**
     * 특정 장소의 상세 정보 조회
     *
     * @param placeId
     * @return
     */
    @GetMapping("/placedetail/{id}")
    public BaseResponse<PlaceDetailView> getPlaceDetail(@PathVariable("id") Long placeId){
        return BaseResponse.ok(placeViewService.getPlaceDetail(placeId));
    }
}
