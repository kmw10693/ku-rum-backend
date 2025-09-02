package ku_rum.backend.domain.rank.application.response;

import ku_rum.backend.domain.rank.domain.PlaceRank;

public record GetPlaceUserRankResponse(String name, int sharingCount) {

    public static GetPlaceUserRankResponse from(PlaceRank placeRank) {
        return new GetPlaceUserRankResponse(placeRank.getPlace().getName(), placeRank.getCount());
    }
}
