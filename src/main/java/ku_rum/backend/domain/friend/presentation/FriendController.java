package ku_rum.backend.domain.friend.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.friend.application.FriendManageService;
import ku_rum.backend.domain.friend.dto.request.FriendBlockRequest;
import ku_rum.backend.domain.friend.dto.request.FriendReportRequest;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendManageService friendManageService;

    /**
     * 친구 차단 API
     */
    @PatchMapping("/block")
    public BaseResponse<String> blockFriend(@RequestBody @Valid final FriendBlockRequest friendBlockRequest) {
        friendManageService.blockFriend(friendBlockRequest);
        return BaseResponse.ok("차단이 완료되었습니다.");
    }

    /**
     * 친구 신고 API
     */
    @PatchMapping("/report")
    public BaseResponse<String> reportFriend(@RequestBody @Valid final FriendReportRequest friendReportRequest) {
        friendManageService.reportFriend(friendReportRequest);
        return BaseResponse.ok("신고가 완료되었습니다.");
    }
}
