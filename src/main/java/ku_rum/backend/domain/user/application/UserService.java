package ku_rum.backend.domain.user.application;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.common.mail.dto.request.EmailValidationRequest;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.user.dto.request.ProfileChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
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

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Timed("waves.user.time_1")
    @Counted("waves.user.count_1")
    @Transactional
    public UserSaveResponse saveUser(final UserSaveRequest userSaveRequest) {
        validateUser(userSaveRequest);

        String password = passwordEncoder.encode(userSaveRequest.password());
        Department department = getDepartment(userSaveRequest);

        User user = UserSaveRequest.newUser(userSaveRequest, department, password);
        return UserSaveResponse.from(userRepository.save(user));
    }

    @Timed("waves.user.time_3")
    @Counted("waves.user.count_3")
    @Transactional
    public void resetAccount(final ResetAccountRequest resetAccountRequest) {
        User user = getUser();
        user.setPassword(passwordEncoder.encode(resetAccountRequest.password()));
    }

    @Timed("waves.user.time_4")
    @Counted("waves.user.count_4")
    @Transactional
    public void setProfile(final ProfileChangeRequest profileChangeRequest) {
        User user = getUser();
        user.setImageUrl(profileChangeRequest.imageUrl());
    }

    @Timed("waves.user.time_2")
    @Counted("waves.user.count_2")
    public void validateEmail(final EmailValidationRequest emailValidationRequest) {
        validateDuplicateEmail(emailValidationRequest.email());
    }

    public void validateUserDetails(CustomUserDetails userDetails) {
        if (!userRepository.existsById(userDetails.getUserId())) {
            throw new NoSuchUserException(NO_SUCH_USER);
        }
    }

    @Timed("waves.user.time_6")
    @Counted("waves.user.count_6")
    public void checkDuplicateNickname(final String nickname) {
        validateNickname(nickname);
    }

    @Timed("waves.user.time_5")
    @Counted("waves.user.count_5")
    public Boolean checkDuplicateId(final String value) {
        return userRepository.existsByLoginId(value);
    }

    @Timed("waves.user.time_7")
    @Counted("waves.user.count_7")
    public void checkDuplicateStudentId(final String studentId) {
        validateDuplicateStudentId(studentId);
    }

    private void validateUser(UserSaveRequest userSaveRequest) {
        validateDuplicateEmail(userSaveRequest.email());
        validateDuplicateLoginId(userSaveRequest.loginId());
        validateDuplicateStudentId(userSaveRequest.studentId());
        validateNickname(userSaveRequest.nickname());
        validateDepartmentName(userSaveRequest.department());
    }

    private void validateDuplicateLoginId(final String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException(DUPLICATE_LOGIN);
        }
    }

    private void validateNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException(DUPLICATE_NICKNAME);
        }
    }

    private void validateDuplicateEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
    }

    private void validateDuplicateStudentId(final String studentId) {
        if (userRepository.existsByStudentId(studentId)) {
            throw new DuplicateStudentIdException(DUPLICATE_STUDENT_ID);
        }
    }

    private void validateDepartmentName(final String department) {
        if (!departmentRepository.existsByName(department)) {
            throw new NoSuchDepartmentException(NO_SUCH_DEPARTMENT);
        }
    }

    private Department getDepartment(UserSaveRequest userSaveRequest) {
        return departmentRepository.findFirstByName(userSaveRequest.department())
                .orElseThrow(() -> new NoSuchDepartmentException(NO_SUCH_DEPARTMENT));
    }

    private User getUser() {
        Long memberId = UserUtils.getLongMemberId();
        return userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }
}
