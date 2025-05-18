package ku_rum.backend.domain.friend.dto.response;

public record SentFriendResponse(Long requestId, Long fromUserId, String fromUserNickname, String imageUrl) {

}
