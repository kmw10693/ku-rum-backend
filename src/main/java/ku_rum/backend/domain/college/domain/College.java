package ku_rum.backend.domain.college.domain;

import jakarta.persistence.*;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "college")
public class College extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    private College(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static College of(String name) {
        return College.builder()
                .name(name)
                .build();
    }
}
