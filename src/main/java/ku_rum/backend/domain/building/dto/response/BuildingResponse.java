package ku_rum.backend.domain.building.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import ku_rum.backend.domain.building.domain.Building;

import java.math.BigDecimal;

public record BuildingResponse(
        Long buildingId,
        String buildingName,
        Long buildingNumber,
        String buildingAbbreviation,
        BigDecimal latitude,
        BigDecimal longitude
) {

  @QueryProjection
  public BuildingResponse(Long buildingId, String buildingName, Long buildingNumber, String buildingAbbreviation, BigDecimal latitude, BigDecimal longitude) {
    this.buildingId = buildingId;
    this.buildingName = buildingName;
    this.buildingNumber = buildingNumber;
    this.buildingAbbreviation = buildingAbbreviation;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static BuildingResponse of(Building building) {
    return new BuildingResponse(
            building.getId(),
            building.getName(),
            building.getNumber(),
            building.getAbbreviation(),
            building.getLatitude(),
            building.getLongitude()
    );
  }
}
