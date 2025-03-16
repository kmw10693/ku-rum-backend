package ku_rum.backend.domain.user.application;

import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.common.mail.dto.request.EmailValidationRequest;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.user.dto.request.ProfileChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.LoginIdResponse;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import ku_rum.backend.global.exception.email.DuplicateEmailException;
import ku_rum.backend.global.exception.user.*;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSaveResponse saveUser(final UserSaveRequest userSaveRequest) {
        log.info("사용자 저장 요청: {}", userSaveRequest);
        validateUser(userSaveRequest);

        String password = encodePassword(userSaveRequest.password());
        Department department = getDepartment(userSaveRequest);

        User user = UserSaveRequest.newUser(userSaveRequest, department, password);
        log.info("사용자 저장 완료: ID={}", userSaveRequest.loginId());
        return UserSaveResponse.from(userRepository.save(user));
    }

    @Transactional
    public void resetAccount(final ResetAccountRequest resetAccountRequest) {
        log.info("계정 초기화 요청: loginId={}", resetAccountRequest.loginId());
        User user = getUserByLoginId(resetAccountRequest.loginId());
        user.setPassword(encodePassword(resetAccountRequest.password()));
        log.info("계정 비밀번호 변경 완료: loginId={}", resetAccountRequest.loginId());
    }


    @Transactional
    public void setProfile(final ProfileChangeRequest profileChangeRequest) {
        log.info("프로필 변경 요청: imageUrl={}", profileChangeRequest.imageUrl());
        User user = getUser();
        user.setImageUrl(profileChangeRequest.imageUrl());
        log.info("프로필 변경 완료: userId={}", user.getId());
    }

    public void validateEmail(final EmailValidationRequest emailValidationRequest) {
        log.info("이메일 중복 검사 요청: email={}", emailValidationRequest.email());
        validateDuplicateEmail(emailValidationRequest.email());
    }

    public void validateUserDetails(CustomUserDetails userDetails) {
        log.info("사용자 검증 요청: userId={}", userDetails.getUserId());
        if (!userRepository.existsById(userDetails.getUserId())) {
            throw new NoSuchUserException(NO_SUCH_USER);
        }
    }

    public void checkDuplicateNickname(final String nickname) {
        log.info("닉네임 중복 검사 요청: nickname={}", nickname);
        validateNickname(nickname);
    }

    public void checkDuplicateId(final String loginId) {
        log.info("로그인 ID 중복 검사 요청: loginId={}", loginId);
        validateDuplicateLoginId(loginId);
    }

    public void checkDuplicateStudentId(final String studentId) {
        log.info("학번 중복 검사 요청: studentId={}", studentId);
        validateDuplicateStudentId(studentId);
    }

    public LoginIdResponse getLoginId(final String email) {
        log.info("로그인 ID 조회 요청: email={}", email);
        User user = getUserByEmail(email);
        log.info("로그인 ID 조회 완료: loginId={}", user.getLoginId());
        return LoginIdResponse.of(user.getLoginId());
    }

    private User getUserByEmail(final String email) {
        log.debug("이메일로 사용자 검색: email={}", email);
        return userRepository.findUserByEmail(email).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }

    private User getUserByLoginId(final String loginId) {
        log.debug("로그인 ID로 사용자 검색: loginId={}", loginId);
        return userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }

    private String encodePassword(final String password) {
        log.debug("비밀번호 암호화 수행");
        return passwordEncoder.encode(password);
    }

    private void validateUser(final UserSaveRequest userSaveRequest) {
        log.info("사용자 유효성 검사 시작: {}", userSaveRequest);
        validateDuplicateEmail(userSaveRequest.email());
        validateDuplicateLoginId(userSaveRequest.loginId());
        validateDuplicateStudentId(userSaveRequest.studentId());
        validateNickname(userSaveRequest.nickname());
        validateDepartmentName(userSaveRequest.department());
    }

    private void validateDuplicateLoginId(final String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            log.warn("중복된 로그인 ID 발견: loginId={}", loginId);
            throw new DuplicateLoginIdException(DUPLICATE_LOGIN);
        }
    }

    private void validateNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            log.warn("중복된 닉네임 발견: nickname={}", nickname);
            throw new DuplicateNicknameException(DUPLICATE_NICKNAME);
        }
    }

    private void validateDuplicateEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("중복된 이메일 발견: email={}", email);
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
    }

    private void validateDuplicateStudentId(final String studentId) {
        if (userRepository.existsByStudentId(studentId)) {
            log.warn("중복된 학번 발견: studentId={}", studentId);
            throw new DuplicateStudentIdException(DUPLICATE_STUDENT_ID);
        }
    }

    private void validateDepartmentName(final String department) {
        if (!departmentRepository.existsByName(department)) {
            log.warn("존재하지 않는 학과: department={}", department);
            throw new NoSuchDepartmentException(NO_SUCH_DEPARTMENT);
        }
    }

    private Department getDepartment(final UserSaveRequest userSaveRequest) {
        log.debug("학과 정보 검색: department={}", userSaveRequest.department());
        return departmentRepository.findFirstByName(userSaveRequest.department())
                .orElseThrow(() -> new NoSuchDepartmentException(NO_SUCH_DEPARTMENT));
    }

    private User getUser() {
        Long memberId = UserUtils.getLongMemberId();
        log.debug("현재 사용자 조회: userId={}", memberId);
        return userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }

}
