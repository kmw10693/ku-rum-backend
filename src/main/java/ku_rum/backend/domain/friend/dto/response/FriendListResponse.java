package ku_rum.backend.domain.friend.dto.response;

import ku_rum.backend.domain.friend.domain.Friend;
import lombok.Builder;

public record FriendListResponse(
        Long id,
        String nickname) {

    @Builder
    public FriendListResponse {
    }

    public static FriendListResponse from(Friend friend) {
        return FriendListResponse.builder()
                .id(friend.getToUser().getId())
                .nickname(friend.getToUser().getNickname())
                .build();
    }
}
