package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.domain.notice.dto.response.RecentSearchTerm;
import ku_rum.backend.domain.place.application.PositionService;
import ku_rum.backend.domain.place.dto.response.CurrentPositionStatusResponse;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PositionService positionService;

    @GetMapping
    public BaseResponse<CurrentPositionStatusResponse> getCurrentPositionStatus(@AuthenticationPrincipal CustomUserDetails userDetails){
        CurrentPositionStatusResponse response = positionService.getCurrentPositionStatus(userDetails);
        return BaseResponse.ok(response);
    }
}