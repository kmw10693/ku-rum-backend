package ku_rum.backend.domain.recruitment.domain;

import jakarta.persistence.*;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "recruitment")
public class Recruitment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024, nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String career;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitCategory recruitCategory;

    @Column(nullable = false)
    private String company;

    @Builder
    private Recruitment(String url, String title, String location, String career, RecruitCategory recruitCategory, String company) {
        this.url = url;
        this.title = title;
        this.location = location;
        this.career = career;
        this.recruitCategory = recruitCategory;
        this.company = company;
    }

    public static Recruitment of(String url, String title, String location, String career, RecruitCategory recruitCategory, String company) {
        return Recruitment.builder()
                .title(title)
                .url(url)
                .location(location)
                .career(career)
                .recruitCategory(recruitCategory)
                .company(company)
                .build();
    }
}
