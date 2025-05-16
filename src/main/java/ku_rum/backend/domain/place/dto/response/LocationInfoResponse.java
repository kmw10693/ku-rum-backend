package ku_rum.backend.domain.place.dto.response;

import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;

public record LocationInfoResponse(
        boolean isShare,
        BuildingViewResponse buildingViewResponse
) {
}
