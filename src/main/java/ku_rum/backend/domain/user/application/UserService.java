package ku_rum.backend.domain.user.application;

import ku_rum.backend.domain.department.application.DepartmentQueryService;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.user.dto.request.NicknameChangeRequest;
import ku_rum.backend.domain.user.dto.request.ProfileChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.LoginIdResponse;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.exception.user.*;
import ku_rum.backend.global.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserQueryService userQueryService;
    private final DepartmentQueryService departmentQueryService;

    @Transactional
    public UserSaveResponse saveUser(final UserSaveRequest userSaveRequest) {
        log.info("사용자 저장 요청: {}", userSaveRequest);
        userValidator.validateUser(userSaveRequest);

        Department department = departmentQueryService.getDepartment(userSaveRequest);

        User user = UserSaveRequest.newUser(userSaveRequest, department, passwordEncoder.encode(userSaveRequest.password()));
        log.info("사용자 저장 완료: ID={}", userSaveRequest.loginId());
        return UserSaveResponse.from(userRepository.save(user));
    }

    @Transactional
    public void resetAccount(final ResetAccountRequest resetAccountRequest) {
        log.info("계정 초기화 요청: loginId={}", resetAccountRequest.loginId());
        User user = userQueryService.getUserByLoginId(resetAccountRequest.loginId());
        userValidator.validatePassword(resetAccountRequest.prevPassword(), user.getPassword());
        user.changePassword(passwordEncoder.encode(resetAccountRequest.newPassword()));
        log.info("계정 비밀번호 변경 완료: loginId={}", resetAccountRequest.loginId());
    }

    @Transactional
    public void changeProfile(final ProfileChangeRequest profileChangeRequest) {
        log.info("프로필 변경 요청: imageUrl={}", profileChangeRequest.imageUrl());
        User user = getUser();
        user.changeImage(profileChangeRequest.imageUrl());
        log.info("프로필 변경 완료: userId={}", user.getId());
    }

    public LoginIdResponse getLoginId(final String email) {
        log.info("로그인 ID 조회 요청: email={}", email);
        User user = userQueryService.getUserByEmail(email);
        log.info("로그인 ID 조회 완료: loginId={}", user.getLoginId());
        return LoginIdResponse.of(user.getLoginId());

    }

    @Transactional
    public void changeNickname(final NicknameChangeRequest nicknameChangeRequest) {
        User user = getUser();
        user.changeNickname(nicknameChangeRequest.nickname());
    }

    private User getUser() {
        Long memberId = UserUtils.getLongMemberId();
        log.debug("현재 사용자 조회: userId={}", memberId);
        return userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }
}
