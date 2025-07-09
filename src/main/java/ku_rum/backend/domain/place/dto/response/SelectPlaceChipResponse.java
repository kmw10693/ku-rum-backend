package ku_rum.backend.domain.place.dto.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import ku_rum.backend.domain.place.domain.Place;

public record SelectPlaceChipResponse(String name,
                                      String subName,
                                      String content,
                                      BigDecimal latitude,
                                      BigDecimal longitude,
                                      List<SelectPlaceChipFriendListResponse> friends
) {

    public static SelectPlaceChipResponse from(Place place) {
        return new SelectPlaceChipResponse(place.getName(), place.getSubName(), place.getContent(), place.getLatitude(),
                place.getLongitude(), Collections.emptyList());
    }

    public static SelectPlaceChipResponse from(Place place,
                                               List<SelectPlaceChipFriendListResponse> selectPlaceChipFriendListResponses) {

        return new SelectPlaceChipResponse(place.getName(), place.getSubName(), place.getContent(), place.getLatitude(),
                place.getLongitude(), selectPlaceChipFriendListResponses);
    }
}
