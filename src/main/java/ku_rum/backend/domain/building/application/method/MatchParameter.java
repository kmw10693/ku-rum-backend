package ku_rum.backend.domain.building.application.method;

import ku_rum.backend.domain.building.dto.response.BuildingResponse;

import java.util.List;

public record MatchParameter(
        String text,
        List<BuildingResponse> list
) {
}
