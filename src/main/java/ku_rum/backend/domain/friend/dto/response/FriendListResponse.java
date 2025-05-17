package ku_rum.backend.domain.friend.dto.response;

import ku_rum.backend.domain.user.domain.User;

public record FriendListResponse(Long id, String nickname, String imageUrl) {
    public static FriendListResponse from(User user) {
        return new FriendListResponse(user.getId(), user.getNickname(), user.getImageUrl());
    }
}
