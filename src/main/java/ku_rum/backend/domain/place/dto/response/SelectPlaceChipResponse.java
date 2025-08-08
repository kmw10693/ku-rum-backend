package ku_rum.backend.domain.place.dto.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ku_rum.backend.domain.place.domain.SubPlace;
import ku_rum.backend.domain.place.domain.PlaceImage;

public record SelectPlaceChipResponse(String name,
                                      String subName,
                                      String content,
                                      BigDecimal latitude,
                                      BigDecimal longitude,
                                      List<String> imgUrls,
                                      List<SelectPlaceChipFriendListResponse> friends
) {

    public static SelectPlaceChipResponse from(SubPlace subPlace, Map<Long, List<PlaceImage>> placeImageMap) {
        List<String> imgUrls = Optional.ofNullable(placeImageMap.get(subPlace.getPlaceId()))
                .orElse(List.of())
                .stream()
                .map(PlaceImage::getImageUrl)
                .toList();
        return new SelectPlaceChipResponse(subPlace.getName(), subPlace.getSubName(), subPlace.getContent(), subPlace.getLatitude(),
                subPlace.getLongitude(), imgUrls, Collections.emptyList());
    }

    public static SelectPlaceChipResponse from(SubPlace subPlace,
                                               List<SelectPlaceChipFriendListResponse> selectPlaceChipFriendListResponses,
                                               Map<Long, List<PlaceImage>> placeImageMap) {
        List<String> imgUrls = Optional.ofNullable(placeImageMap.get(subPlace.getPlaceId()))
                .orElse(List.of())
                .stream()
                .map(PlaceImage::getImageUrl)
                .toList();
        return new SelectPlaceChipResponse(subPlace.getName(), subPlace.getSubName(), subPlace.getContent(), subPlace.getLatitude(),
                subPlace.getLongitude(), imgUrls, selectPlaceChipFriendListResponses);
    }
}
