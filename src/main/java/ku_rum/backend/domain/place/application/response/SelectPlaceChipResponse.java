package ku_rum.backend.domain.place.application.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import ku_rum.backend.domain.place.domain.SubPlace;

public record SelectPlaceChipResponse(
        Long placeId,
        String name,
        String subName,
        String content,
        BigDecimal latitude,
        BigDecimal longitude,
        List<SelectPlaceChipFriendListResponse> friends
) {

    public static SelectPlaceChipResponse from(SubPlace subPlace) {
        return new SelectPlaceChipResponse(subPlace.getPlaceId(), subPlace.getName(), subPlace.getSubName(),
                subPlace.getAbbreviation(),
                subPlace.getLatitude(), subPlace.getLongitude(), Collections.emptyList());
    }

    public static SelectPlaceChipResponse from(SubPlace subPlace,
                                               List<SelectPlaceChipFriendListResponse> selectPlaceChipFriendListResponses) {
        return new SelectPlaceChipResponse(subPlace.getPlaceId(), subPlace.getName(), subPlace.getSubName(),
                subPlace.getAbbreviation(),
                subPlace.getLatitude(), subPlace.getLongitude(), selectPlaceChipFriendListResponses);
    }
}
