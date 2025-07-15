package ku_rum.backend.domain.place.application.response;

import ku_rum.backend.domain.place.dto.FriendUserDto;

public record SelectPlaceChipFriendListResponse(String nickname,
                                                String profileUrl) {
    public static SelectPlaceChipFriendListResponse from(FriendUserDto friendUserDto) {
        return new SelectPlaceChipFriendListResponse(friendUserDto.nickname(), friendUserDto.profileImageUrl());
    }
}
