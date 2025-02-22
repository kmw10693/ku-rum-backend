package ku_rum.backend.domain.category.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private BuildingCategory(Building building, Category category) {
        this.building = building;
        this.category = category;
    }

    public static BuildingCategory of(Building building, Category category) {
        return new BuildingCategory(building, category);
    }
}
