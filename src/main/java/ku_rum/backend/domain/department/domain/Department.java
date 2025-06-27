package ku_rum.backend.domain.department.domain;

import jakarta.persistence.*;
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
    @JoinColumn(name = "college_id")
    private College college;

    @Builder
    private Department(String name, College college) {
        this.name = name;
        this.college = college;
    }

    public static Department of(String name, College college) {
        return Department.builder()
                .name(name)
                .college(college)
                .build();
    }
}
