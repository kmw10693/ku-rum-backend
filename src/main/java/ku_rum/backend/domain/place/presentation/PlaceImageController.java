/*
package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.domain.place.application.PlaceImageService;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places/imgs")
@RequiredArgsConstructor
public class PlaceImageController {
    private final PlaceImageService placeImageService;

    */
/**
     * 특정 장소와 관련된 이미지 조회
     *
     * @param placeId
     * @return
     *//*

    @GetMapping("/id={placeId}")
    public BaseResponse<List<String>> getImageByFileName(@PathVariable("placeId") Long placeId) {
        List<String> url = placeImageService.getPresignedImageUrls(placeId);
        return BaseResponse.ok(url);
    }
}
*/
