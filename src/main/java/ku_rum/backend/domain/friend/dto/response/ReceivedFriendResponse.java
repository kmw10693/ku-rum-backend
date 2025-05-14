package ku_rum.backend.domain.friend.dto.response;

import ku_rum.backend.domain.friend.domain.Friend;

public record ReceivedFriendResponse(Long requestId, Long fromUserId, String fromUserNickname) {

    public static ReceivedFriendResponse from(Friend friend) {
        return new ReceivedFriendResponse(
                friend.getId(),
                friend.getFromUser().getId(),
                friend.getFromUser().getNickname()
        );
    }
}
