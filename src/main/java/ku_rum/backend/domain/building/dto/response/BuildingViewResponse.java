package ku_rum.backend.domain.building.dto.response;

import ku_rum.backend.domain.building.domain.Building;

import java.math.BigDecimal;

public record BuildingViewResponse(
        Long id,
        String abbreviation,
        String name,
        Long number,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static BuildingViewResponse from(Building building) {
        return new BuildingViewResponse(
                building.getId(),
                building.getAbbreviation(),
                building.getName(),
                building.getNumber(),
                building.getLatitude(),
                building.getLongitude()
        );
    }
}
