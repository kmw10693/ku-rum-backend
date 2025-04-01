package ku_rum.backend.domain.user.application;

import jakarta.transaction.Transactional;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingRepository;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.common.mail.dto.request.EmailValidationRequest;
import ku_rum.backend.domain.user.dto.request.NicknameChangeRequest;
import ku_rum.backend.domain.user.dto.request.InitiatePasswordResetRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.LoginIdResponse;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.batch.BatchScheduler;
import ku_rum.backend.global.exception.email.DuplicateEmailException;
import ku_rum.backend.global.exception.user.DuplicateNicknameException;
import ku_rum.backend.global.exception.user.DuplicateStudentIdException;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @MockBean
    private BatchScheduler batchScheduler;

    private Building building;

    private Department department;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        building = Building.of("신공학관", 3L, "신공", 3L, BigDecimal.valueOf(64.3423423), BigDecimal.valueOf(64.3423423));
        buildingRepository.save(building);

        department = Department.of("컴퓨터공학부", building);
        departmentRepository.save(department);
    }

    @Test
    @DisplayName("회원을 올바르게 저장을 요청하면 저장한다.")
    void saveMember() {
        //given
        UserSaveRequest request = UserSaveRequest.builder()
                .loginId("kmw10693")
                .email("kmw10693@konkuk.ac.kr")
                .password("password123")
                .nickname("미미미누")
                .studentId("202112322")
                .department("컴퓨터공학부")
                .build();
        //when
        UserSaveResponse userSaveResponse = userService.saveUser(request);

        //then
        assertThat(userSaveResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("회원의 이메일이 이미 있는 경우 예외를 처리한다.")
    void validateEmail() {
        //given
        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);

        EmailValidationRequest emailValidationRequest = new EmailValidationRequest("kmw10693@konkuk.ac.kr");

        //when then
        assertThatThrownBy(() -> userValidator.validateDuplicateEmail(emailValidationRequest.email()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("회원의 닉네임이 이미 있는 경우 예외를 처리한다.")
    void validateNickname() {
        //given
        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);

        //when then
        assertThatThrownBy(() -> userValidator.validateNickname("미미미누"))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @Test
    @DisplayName("회원의 학번이 이미 있는 경우 예외를 처리한다.")
    void validateStudentId() {
        //given
        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);

        //when then
        assertThatThrownBy(() -> userValidator.validateDuplicateStudentId("202112322"))
                .isInstanceOf(DuplicateStudentIdException.class);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 아이디를 가져오는 경우 성공한다.")
    void getLoginId() {
        //given
        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);

        LoginIdResponse loginId = userService.getLoginId("kmw10693@konkuk.ac.kr");

        //when then
        assertThat(loginId.loginId()).isEqualTo("kmw106933");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 아이디를 가져오는 경우 실패한다.")
    void getLoginIdFail() {
        //given
        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);


        //when then
        assertThatThrownBy(() -> userService.getLoginId("kmw106943@konkuk.ac.kr"))
                .isInstanceOf(NoSuchUserException.class);
    }

    @Test
    @DisplayName("비밀번호를 성공적으로 변경한다.")
    void changeLoginIdSuccess() {
        //given
        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password(passwordEncoder.encode("password123"))
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);
        System.out.println(user.getPassword());
        InitiatePasswordResetRequest request = new InitiatePasswordResetRequest("kmw106933", "password1234");

        //when
        userService.initiatePasswordReset(request);
        //then
        assertThat(passwordEncoder.matches("password1234", user.getPassword())).isEqualTo(true);
    }

    @Test
    @DisplayName("닉네임 성공적으로 변경한다.")
    @Transactional
    void changeNicknameSuccess() {
        //given

        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);

        CustomUserDetails userDetails = CustomUserDetails.of(user.getId(), "testUser", AuthorityUtils.createAuthorityList("ROLE_USER"), "test12345");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        NicknameChangeRequest request = new NicknameChangeRequest("abcd1234");

        //when
        userService.changeNickname(request);
        //then
        assertThat(user.getNickname()).isEqualTo("abcd1234");
    }

    @Test
    @DisplayName("회원 탈퇴를 성공적으로 진행한다.")
    @Transactional
    void deactivateUserSuccess() {
        //given

        User user = User.builder()
                .loginId("kmw106933")
                .email("kmw10693@konkuk.ac.kr")
                .nickname("미미미누")
                .password("password123")
                .studentId("202112322")
                .department(department)
                .build();

        userRepository.save(user);

        CustomUserDetails userDetails = CustomUserDetails.of(user.getId(), "testUser", AuthorityUtils.createAuthorityList("ROLE_USER"), "test12345");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        //when
        userService.deactivate();
        //then
        assertThat(user.isActive()).isEqualTo(false);
    }

}