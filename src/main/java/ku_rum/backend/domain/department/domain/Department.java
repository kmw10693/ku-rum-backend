package ku_rum.backend.domain.department.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.college.domain.College;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.*;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buliding_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    @Builder
    private Department(String name, Building building, College college) {
        this.name = name;
        this.building = building;
        this.college = college;
    }

    public static Department of(String name, Building building, College college) {
        return Department.builder()
                .name(name)
                .building(building)
                .college(college)
                .build();
    }
}
