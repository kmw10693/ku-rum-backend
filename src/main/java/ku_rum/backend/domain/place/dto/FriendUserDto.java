package ku_rum.backend.domain.place.dto;

public record FriendUserDto(Long userId, String nickname, String profileImageUrl,
                            Long placeId) {
}
