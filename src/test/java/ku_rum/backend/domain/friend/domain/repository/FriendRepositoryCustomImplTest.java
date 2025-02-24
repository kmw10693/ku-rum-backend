package ku_rum.backend.domain.friend.domain.repository;

import jakarta.persistence.EntityManager;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
public class FriendRepositoryCustomImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FriendRepositoryCustomImpl friendRepositoryCustom;

    @Test
    void findFriends_친구_목록_조회() {
        // Given
        Building building = Building.of("신공학관", 3L,"신공", 1L, BigDecimal.valueOf(234.3), BigDecimal.valueOf(342.23));
        entityManager.persist(building);
        Department department = Department.of("컴퓨터공학부", building);
        entityManager.persist(department);

        User user1 = User.builder()
                .loginId("user1")
                .email("user1@test.com")
                .nickname("User One")
                .password("password")
                .studentId("2023001")
                .department(department)
                .build();
        User user2 = User.builder()
                .loginId("user2")
                .email("user2@test.com")
                .nickname("User Two")
                .password("password")
                .studentId("2023002")
                .department(department)
                .build();
        User user3 = User.builder()
                .loginId("user3")
                .email("user3@test.com")
                .nickname("User Three")
                .password("password")
                .studentId("2023003")
                .department(department)
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);

        Friend friend1 = Friend.of(user1, user2, FriendStatus.ACCEPT);
        Friend friend2 = Friend.of(user1, user3, FriendStatus.ACCEPT);

        entityManager.persist(friend1);
        entityManager.persist(friend2);
        entityManager.flush();

        // When
        List<Friend> friends = friendRepositoryCustom.findFriends(FriendStatus.ACCEPT, user1.getId());

        // Then
        assertThat(friends).hasSize(2);
        assertThat(friends.get(0).getFromUser().getId()).isEqualTo(user1.getId());
        assertThat(friends.get(0).getToUser().getId()).isEqualTo(user2.getId());
    }

    @Test
    void existFriends_친구_존재여부() {
        // Given
        Building building = Building.of("신공학관", 3L,"신공", 1L, BigDecimal.valueOf(234.3), BigDecimal.valueOf(342.23));
        entityManager.persist(building);
        Department department = Department.of("컴퓨터공학부", building);
        entityManager.persist(department);

        User user1 = User.builder()
                .loginId("user1")
                .email("user1@test.com")
                .nickname("User One")
                .password("password")
                .studentId("2023001")
                .department(department)
                .build();
        User user2 = User.builder()
                .loginId("user2")
                .email("user2@test.com")
                .nickname("User Two")
                .password("password")
                .studentId("2023002")
                .department(department)
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);

        Friend friend = Friend.of(user1, user2, FriendStatus.ACCEPT);
        entityManager.persist(friend);
        entityManager.flush();

        // When
        Boolean exists = friendRepositoryCustom.existFriends(FriendStatus.ACCEPT, user1.getId(), user2.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existFriends_친구_존재하지않음() {
        // Given
        Building building = Building.of("신공학관", 3L,"신공", 1L, BigDecimal.valueOf(234.3), BigDecimal.valueOf(342.23));
        entityManager.persist(building);
        Department department = Department.of("컴퓨터공학부", building);
        entityManager.persist(department);

        User user1 = User.builder()
                .loginId("user1")
                .email("user1@test.com")
                .nickname("User One")
                .password("password")
                .studentId("2023001")
                .department(department)
                .build();
        User user2 = User.builder()
                .loginId("user2")
                .email("user2@test.com")
                .nickname("User Two")
                .password("password")
                .studentId("2023002")
                .department(department)
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);

        Friend friend = Friend.of(user1, user2, FriendStatus.ACCEPT);
        entityManager.persist(friend);
        entityManager.flush();

        // When
        Boolean exists = friendRepositoryCustom.existFriends(FriendStatus.PENDING, user1.getId(), user2.getId());

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findOriginFriends_친구_상태조회() {
        // Given
        Building building = Building.of("신공학관", 3L,"신공", 1L, BigDecimal.valueOf(234.3), BigDecimal.valueOf(342.23));
        entityManager.persist(building);
        Department department = Department.of("컴퓨터공학부", building);
        entityManager.persist(department);

        User user1 = User.builder()
                .loginId("user1")
                .email("user1@test.com")
                .nickname("User One")
                .password("password")
                .studentId("2023001")
                .department(department)
                .build();
        User user2 = User.builder()
                .loginId("user2")
                .email("user2@test.com")
                .nickname("User Two")
                .password("password")
                .studentId("2023002")
                .department(department)
                .build();
        User user3 = User.builder()
                .loginId("user3")
                .email("user3@test.com")
                .nickname("User Three")
                .password("password")
                .studentId("2023003")
                .department(department)
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);

        Friend friend1 = Friend.of(user1, user2, FriendStatus.ACCEPT);
        Friend friend2 = Friend.of(user2, user3, FriendStatus.PENDING);

        entityManager.persist(friend1);
        entityManager.persist(friend2);
        entityManager.flush();

        // When
        List<Friend> originFriends = friendRepositoryCustom.findOriginFriends(FriendStatus.ACCEPT, user1.getId(), user2.getId());

        // Then
        assertThat(originFriends).hasSize(1);
        assertThat(originFriends.get(0).getFromUser().getId()).isEqualTo(user1.getId());
        assertThat(originFriends.get(0).getToUser().getId()).isEqualTo(user2.getId());
    }
}