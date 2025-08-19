package ku_rum.backend.domain.place.application.response;

import java.math.BigDecimal;
import java.util.List;
import ku_rum.backend.domain.place.domain.SubPlace;
import ku_rum.backend.domain.place.domain.PlaceImage;
import ku_rum.backend.domain.place.dto.FriendUserDto;
import lombok.Builder;

@Builder
public record GetPlaceResponse(Long placeId,
                               String name,
                               String subName,
                               String content,
                               BigDecimal latitude,
                               BigDecimal longitude,
                               List<SelectPlaceChipFriendListResponse> friends,
                               List<String> imageUrls) {

    public static GetPlaceResponse of(SubPlace subPlace,
                                      List<FriendUserDto> friendUserDtos,
                                      List<PlaceImage> placeImages) {
        List<SelectPlaceChipFriendListResponse> selectPlaceChipFriendListResponses = friendUserDtos.stream()
                .map(SelectPlaceChipFriendListResponse::from)
                .toList();

        List<String> ImageUrls = placeImages.stream().map(PlaceImage::getImageUrl).toList();
        return new GetPlaceResponse(subPlace.getPlaceId(), subPlace.getName(), subPlace.getSubName(), subPlace.getContent(),
                subPlace.getLatitude(), subPlace.getLongitude(), selectPlaceChipFriendListResponses, ImageUrls);
    }
}
