package ku_rum.backend.domain.place.application.response;

import ku_rum.backend.domain.place.domain.PlaceHistory;

public record SearchPlaceHistoryResponse(String name, Long placeHistoryId) {

    public static SearchPlaceHistoryResponse from(PlaceHistory placeHistory) {
        return new SearchPlaceHistoryResponse(placeHistory.getName(), placeHistory.getPlaceHistoryId());
    }
}
