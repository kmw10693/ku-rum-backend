package ku_rum.backend.domain.recruitment.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class RecruitmentTest {

    @Autowired
    private EntityManager entityManager;

    private Recruitment createSampleRecruitment() {
        return Recruitment.of(
                "https://example.com/job/123",
                "백엔드 개발자 공고",
                "서울",
                "경력 5년 이상",
                RecruitCategory.SARAMIN,
                "예시 회사"
        );
    }

    @Test
    void 엔티티_저장_및_조회_테스트() {
        // given
        Recruitment recruitment = createSampleRecruitment();

        // when
        entityManager.persist(recruitment);
        entityManager.flush();
        Recruitment found = entityManager.find(Recruitment.class, recruitment.getId());

        // then
        assertThat(found).isNotNull();
        assertThat(found.getUrl()).isEqualTo(recruitment.getUrl());
        assertThat(found.getTitle()).isEqualTo(recruitment.getTitle());
        assertThat(found.getLocation()).isEqualTo(recruitment.getLocation());
        assertThat(found.getCareer()).isEqualTo(recruitment.getCareer());
        assertThat(found.getRecruitCategory()).isEqualTo(recruitment.getRecruitCategory());
        assertThat(found.getCompany()).isEqualTo(recruitment.getCompany());
    }

    @Test
    void 빌더_사용_생성_테스트() {
        // given
        Recruitment recruitment = Recruitment.builder()
                .url("https://example.com/job/456")
                .title("프론트엔드 개발자")
                .location("부산")
                .career("신입 가능")
                .recruitCategory(RecruitCategory.SARAMIN)
                .company("테스트 회사")
                .build();

        // when
        entityManager.persist(recruitment);
        entityManager.flush();
        Recruitment found = entityManager.find(Recruitment.class, recruitment.getId());

        // then
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("프론트엔드 개발자");
    }
}
