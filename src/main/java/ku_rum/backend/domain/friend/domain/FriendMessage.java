package ku_rum.backend.domain.friend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendMessage {

    SUCCESS("친구 요청이 성공적으로 완료되었습니다."),
    SUCCESS_ACCEPT("친구 승낙 되었습니다."),
    SUCCESS_DENY("친구 거절 되었습니다."),
    SUCCESS_DELETE("친구 삭제 되었습니다.");

    private final String message;
}
