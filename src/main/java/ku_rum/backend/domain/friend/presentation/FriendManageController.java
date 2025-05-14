package ku_rum.backend.domain.friend.presentation;

import ku_rum.backend.domain.friend.application.FriendManageService;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendManageController {

    private final FriendManageService friendManageService;

    /**
     * 친구 요청 API
     */
    @PostMapping("/request")
    public BaseResponse<Void> sendRequest(@RequestParam Long receiverId) {
        friendManageService.requestFriend(receiverId);
        return BaseResponse.ok();
    }

    /**
     * 친구 요청 수락 API
     */
    @PostMapping("/accept")
    public BaseResponse<Void> acceptRequest(@RequestParam Long requestId) {
        friendManageService.respondToFriend(requestId, true);
        return BaseResponse.ok();
    }

    /**
     * 친구 요청 거절 API
     */
    @PostMapping("/reject")
    public BaseResponse<Void> rejectRequest(@RequestParam Long requestId) {
        friendManageService.respondToFriend(requestId, false);
        return BaseResponse.ok();
    }

    /**
     * 친구 요청 삭제 API
     */
    @DeleteMapping("/request")
    public BaseResponse<Void> deleteSentRequest(@RequestParam Long requestId) {
        friendManageService.deleteSentRequest(requestId);
        return BaseResponse.ok();
    }
}
