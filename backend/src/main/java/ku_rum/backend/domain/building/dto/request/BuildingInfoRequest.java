package ku_rum.backend.domain.building.dto.request;

import ku_rum.backend.domain.building.domain.Building;
import lombok.Builder;

import java.math.BigDecimal;

public record BuildingInfoRequest(
        String buildingName,
        Long buildingNumber,
        String bulidingAbbreviation,
        BigDecimal latitude,
        BigDecimal longtitude

) {
  @Builder
  public BuildingInfoRequest(String buildingName, Long buildingNumber, String bulidingAbbreviation, BigDecimal latitude, BigDecimal longtitude) {
    this.buildingName = buildingName;
    this.buildingNumber = buildingNumber;
    this.bulidingAbbreviation = bulidingAbbreviation;
    this.latitude = latitude;
    this.longtitude = longtitude;
  }

  public static BuildingInfoRequest of(Building building) {
    return BuildingInfoRequest.builder()
            .buildingName(building.getName())
            .buildingNumber(building.getNumber())
            .bulidingAbbreviation(building.getAbbreviation())
            .latitude(building.getLatitude())
            .longtitude(building.getLongitude())
            .build();
  }
}
