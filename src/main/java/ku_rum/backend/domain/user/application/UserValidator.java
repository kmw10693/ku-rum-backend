package ku_rum.backend.domain.user.application;

import ku_rum.backend.domain.department.application.DepartmentValidator;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import ku_rum.backend.global.exception.email.DuplicateEmailException;
import ku_rum.backend.global.exception.user.*;
import ku_rum.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {
    private final UserRepository userRepository;
    private final DepartmentValidator departmentValidator;
    private final PasswordEncoder passwordEncoder;

    public void validateUser(final UserSaveRequest userSaveRequest) {
        log.info("사용자 유효성 검사 시작: {}", userSaveRequest);
        validateDuplicateEmail(userSaveRequest.email());
        validateDuplicateLoginId(userSaveRequest.loginId());
        validateDuplicateStudentId(userSaveRequest.studentId());
        validateNickname(userSaveRequest.nickname());
        departmentValidator.validateDepartmentName(userSaveRequest.department());
    }

    public void validateUserDetails(CustomUserDetails userDetails) {
        log.info("사용자 검증 요청: userId={}", userDetails.getUserId());
        if (!userRepository.existsById(userDetails.getUserId())) {
            throw new NoSuchUserException(NO_SUCH_USER);
        }
    }

    public void validateDuplicateLoginId(final String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            log.warn("중복된 로그인 ID 발견: loginId={}", loginId);
            throw new DuplicateLoginIdException(DUPLICATE_LOGIN);
        }
    }

    public void validateNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            log.warn("중복된 닉네임 발견: nickname={}", nickname);
            throw new DuplicateNicknameException(DUPLICATE_NICKNAME);
        }
    }

    public void validateDuplicateEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("중복된 이메일 발견: email={}", email);
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
    }

    public void validateDuplicateStudentId(final String studentId) {
        if (userRepository.existsByStudentId(studentId)) {
            log.warn("중복된 학번 발견: studentId={}", studentId);
            throw new DuplicateStudentIdException(DUPLICATE_STUDENT_ID);
        }
    }

    public void validatePassword(final String rawPassword, final String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException(INVALID_PASSWORD);
        }
    }
}
