package ku_rum.backend.domain.user.application;

import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.mail.dto.request.LoginIdValidationRequest;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import ku_rum.backend.global.exception.user.DuplicateEmailException;
import ku_rum.backend.global.exception.user.DuplicateStudentIdException;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.*;

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
        validateUser(userSaveRequest);

        String password = passwordEncoder.encode(userSaveRequest.password());
        Department department = departmentRepository.findByName(userSaveRequest.department())
               .orElseThrow(() -> new NoSuchDepartmentException(NO_SUCH_DEPARTMENT));

        User user = UserSaveRequest.newUser(userSaveRequest, department, password);
        return UserSaveResponse.from(userRepository.save(user));
    }

    @Transactional
    public void resetAccount(final ResetAccountRequest resetAccountRequest) {

    }

    public void validateEmail(final LoginIdValidationRequest emailValidationRequest) {
        validateDuplicateEmail(emailValidationRequest.loginId());
    }

    public void validateUserDetails(CustomUserDetails userDetails){
        if (!userRepository.existsById(userDetails.getUserId())){
            throw new NoSuchUserException(NO_SUCH_USER);
        }
    }

    private void validateUser(UserSaveRequest userSaveRequest) {
        validateDuplicateEmail(userSaveRequest.email());
        validateDuplicateStudentId(userSaveRequest.studentId());
        validateDepartmentName(userSaveRequest.department());
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

}
