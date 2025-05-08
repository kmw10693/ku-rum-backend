package ku_rum.backend.domain.category.dto.response;

import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;
import ku_rum.backend.domain.place.domain.Place;

import java.math.BigDecimal;

public record CategoryViewPlaceResponse(
        Long placeId,
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        BuildingViewResponse building
) {
    public static CategoryViewPlaceResponse from(Place place) {
        return new CategoryViewPlaceResponse(
                place.getId(),
                place.getName(),
                place.getLatitude(),
                place.getLongitude(),
                BuildingViewResponse.from(place.getBuilding())
        );
    }
}

