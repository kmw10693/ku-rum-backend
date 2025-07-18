package ku_rum.backend.domain.place.application.response;

import java.math.BigDecimal;
import ku_rum.backend.domain.place.domain.Place;

public record SearchPlaceResponse(String name, Long placeId, BigDecimal latitude, BigDecimal longitude) {

    public static SearchPlaceResponse from(Place place) {
        return new SearchPlaceResponse(place.getName(), place.getPlaceId(), place.getLatitude(), place.getLongitude());
    }
}
