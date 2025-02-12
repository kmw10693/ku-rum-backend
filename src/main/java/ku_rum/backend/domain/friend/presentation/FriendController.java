package ku_rum.backend.domain.friend.presentation;

import ku_rum.backend.domain.friend.application.FriendService;
import ku_rum.backend.domain.friend.domain.FriendMessage;
import ku_rum.backend.domain.friend.dto.response.FriendFindResponse;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Validated
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    public BaseResponse<List<FriendListResponse>> getFriends() {
        return BaseResponse.ok(friendService.getMyLists());
    }

    @GetMapping("/find")
    public BaseResponse<FriendFindResponse> findFriends(@RequestParam("nickname") String nickname) {
        return BaseResponse.ok(friendService.findByNameInLists(nickname));
    }

    @PostMapping("/{request_id}")
    public BaseResponse<String> requestFriends(@PathVariable("request_id") Long requestId) {
        friendService.requestFriends(requestId);
        return BaseResponse.ok(FriendMessage.SUCCESS.getMessage());
    }

    @PutMapping("/{request_id}/accept")
    public BaseResponse<String> accept(@PathVariable("request_id") Long requestId) {
        friendService.accept(requestId);
        return BaseResponse.ok(FriendMessage.SUCCESS.getMessage());
    }

    @PutMapping("/{request_id}/deny")
    public BaseResponse<String> deny(@PathVariable("request_id") Long requestId) {
        friendService.deny(requestId);
        return BaseResponse.ok(FriendMessage.SUCCESS_DENY.getMessage());
    }

    @DeleteMapping("/{request_id}")
    public BaseResponse<String> delete(@PathVariable("request_id") Long requestId) {
        friendService.delete(requestId);
        return BaseResponse.ok(FriendMessage.SUCCESS_DELETE.getMessage());
    }
}
