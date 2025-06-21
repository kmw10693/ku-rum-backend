package ku_rum.backend.domain.user.presentation;

import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.dto.request.UserLocationRequest;
import ku_rum.backend.domain.user.dto.request.UserLocationShareStartRequest;
import ku_rum.backend.domain.user.dto.response.UserLocationResponse;
import ku_rum.backend.domain.user.dto.response.UserLocationShareStartResponse;
import ku_rum.backend.domain.user.dto.response.UserShareActiveResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@Validated
public class UserMapController {

    private final UserService userService;

    /**
     * 1. 위치 공유 현재 상태 반환
     *
     * @return
     */
    @GetMapping
    public BaseResponse<UserShareActiveResponse> isShareActive() {
        return BaseResponse.ok(userService.isShareActive());
    }

    /**
     * 2. ‘내 위치 공유’ 클릭
     *
     * @param request
     * @return
     */
    @PostMapping("/share")
    public BaseResponse<UserLocationResponse> userLocation(@RequestBody final UserLocationRequest request) {
        return BaseResponse.ok(userService.getUserLocation(request));
    }


    /**
     * 3. ‘내 위치 공유’ 클릭후 ‘확인’ 클릭
     *
     * @param request
     * @return
     */
    @PostMapping("/shareStart")
    public BaseResponse<UserLocationShareStartResponse> userLocation(@RequestBody final UserLocationShareStartRequest request) {
        return BaseResponse.ok(userService.startShareLocation(request));
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("/notshare")
    public BaseResponse<UserShareActiveResponse> userLocation() {
        return BaseResponse.ok(userService.changeToNotActive());
    }
}
