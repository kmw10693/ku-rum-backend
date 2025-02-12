package ku_rum.backend.domain.friend.dto.request;

import lombok.Builder;

public record FriendFindRequest
        (String nickname) {

    @Builder
    public FriendFindRequest {}

    public static FriendFindRequest from
            (String nickname) {
        return FriendFindRequest
                .builder()
                .nickname(nickname)
                .build();
    }
}
