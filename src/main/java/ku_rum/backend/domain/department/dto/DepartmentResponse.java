package ku_rum.backend.domain.department.dto;

import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.UserDepartment;

public record DepartmentResponse(Long departmentId, String departmentName) {
    public static DepartmentResponse of(Department department) {
        return new DepartmentResponse(department.getId(), department.getName());
    }
}
