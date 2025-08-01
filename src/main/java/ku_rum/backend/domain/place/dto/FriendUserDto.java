package ku_rum.backend.domain.place.dto;

import ku_rum.backend.domain.place.domain.SubPlace;

public record FriendUserDto(Long userId, String nickname, String profileImageUrl,
                            SubPlace subPlace) {
}
