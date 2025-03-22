package ku_rum.backend.domain.department.application;

import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_DEPARTMENT;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentValidator {
    private final DepartmentRepository departmentRepository;

    public void validateDepartmentName(final String department) {
        if (!departmentRepository.existsByName(department)) {
            log.warn("존재하지 않는 학과: department={}", department);
            throw new NoSuchDepartmentException(NO_SUCH_DEPARTMENT);
        }
    }
}
