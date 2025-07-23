package ku_rum.backend.domain.rank.presentation;

import java.util.List;
import ku_rum.backend.domain.rank.application.RankService;
import ku_rum.backend.domain.rank.application.response.GetPlaceUserRankResponse;
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
public class RankController {

    private final RankService rankService;

    @GetMapping("/users/ranks")
    public BaseResponse<List<GetPlaceUserRankResponse>> getPlaceUserRank(
            @AuthenticationPrincipal final CustomUserDetails userDetails) {
        return BaseResponse.ok(rankService.getPlaceUserRank(userDetails));
    }
}
