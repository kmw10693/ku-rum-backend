package ku_rum.backend.domain.department.application;

import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.department.dto.CollegeDepartmentResponse;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_DEPARTMENT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DepartmentQueryService {

    private final DepartmentRepository departmentRepository;

    public Department getDepartment(final UserSaveRequest userSaveRequest) {
        log.debug("학과 정보 검색: department={}", userSaveRequest.department());
        return departmentRepository.findFirstByName(userSaveRequest.department())
                .orElseThrow(() -> new NoSuchDepartmentException(NO_SUCH_DEPARTMENT));
    }

    public CollegeDepartmentResponse getDepartmentsByCollege(final String college) {
        List<Department> departments = departmentRepository.findAllByCollege_Name(college);
        List<String> list = departments.stream().map(Department::getName).toList();
        return new CollegeDepartmentResponse(list);
    }
}
