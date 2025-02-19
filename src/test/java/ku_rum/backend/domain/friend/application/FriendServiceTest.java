package ku_rum.backend.domain.friend.application;

import jakarta.transaction.Transactional;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingRepository;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.dto.request.FriendFindRequest;
import ku_rum.backend.domain.friend.dto.request.FriendListRequest;
import ku_rum.backend.domain.friend.dto.response.FriendFindResponse;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.friend.NoFriendsException;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import ku_rum.backend.global.security.jwt.UserUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendServiceTest {

    @Autowired
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private FriendService friendService;

    private Department department;

    @BeforeAll
    void init() {

        Building building = Building.of("신공학관",11L, "신공",1L, BigDecimal.valueOf(64.3423423), BigDecimal.valueOf(64.3423423));
        buildingRepository.save(building);

        department = Department.of("컴퓨터공학부", building);
        departmentRepository.save(department);

        User fromUser = User.of("kmw106933", "kmw106933@konkuk.ac.kr", "미미미누", "password123", "202112322", department);
        User toUser1 = User.of("kmw106934", "kmw1069332@konkuk.ac.kr","미미미누1", "password123", "202112321", department);
        User toUser2 = User.of("kmw106935", "kmw1069333@konkuk.ac.kr","미미미누2", "password123", "202112323", department);
        User newUser = User.of("kmw106936", "kmw1069334@konkuk.ac.kr","미미미누3", "password123", "202112324", department);

        userRepository.save(fromUser);
        userRepository.save(toUser1);
        userRepository.save(toUser2);
        userRepository.save(newUser);

        Friend friend = Friend.of(fromUser, toUser1, FriendStatus.ACCEPT);
        Friend friend2 = Friend.of(fromUser, toUser2, FriendStatus.ACCEPT);

        friendRepository.save(friend);
        friendRepository.save(friend2);

        MockedStatic<UserUtils> mockedStatic = mockStatic(UserUtils.class);
        mockedStatic.when(UserUtils::getLongMemberId).thenReturn(1L);
    }

}