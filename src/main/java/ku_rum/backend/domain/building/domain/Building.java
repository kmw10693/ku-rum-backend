package ku_rum.backend.domain.building.domain;

import jakarta.persistence.*;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "building")
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buildingId;

    @Column(length = 5)
    private String abbreviation;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Builder
    private Building(String abbreviation, String name, Integer number, BigDecimal latitude, BigDecimal longitude) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Building of(String abbreviation, String name, Integer number, BigDecimal latitude, BigDecimal longitude) {
        return Building.builder()
                .abbreviation(abbreviation)
                .name(name)
                .number(number)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public void updateInfo(String name, Integer number, String abbreviation, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.number = number;
        this.abbreviation = abbreviation;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
