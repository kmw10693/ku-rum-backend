package ku_rum.backend.domain.department.application;

import jakarta.transaction.Transactional;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.UserDepartment;
import ku_rum.backend.domain.department.domain.repository.UserDepartmentRepository;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_DEPARTMENT;

@Service
@RequiredArgsConstructor
public class UserDepartmentService {
    private final UserDepartmentRepository userDepartmentRepository;

    @Transactional
    public void addDeptToUser(User user, Department dept) {
        userDepartmentRepository.save(UserDepartment.of(user, dept));
    }

    @Transactional
    public void deleteDeptFromUser(User user, Department dept) {
        int deleted = userDepartmentRepository.deleteByUserIdAndDepartmentId(user.getId(), dept.getId());
        if (deleted == 0) {
            throw new NoSuchDepartmentException(NO_SUCH_DEPARTMENT);
        }
    }
}
