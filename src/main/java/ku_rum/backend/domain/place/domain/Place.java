package ku_rum.backend.domain.place.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.category.domain.Category;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "place")
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    private Place(String name, BigDecimal latitude, BigDecimal longitude, Building building, Category category) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.building = building;
        this.category = category;
    }

    public static Place of(String name, BigDecimal latitude, BigDecimal longitude, Building building, Category category) {
        return Place.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .building(building)
                .category(category)
                .build();
    }

    public void update(String name, BigDecimal latitude, BigDecimal longitude, Building building, Category category) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.building = building;
        this.category = category;
    }
}
