package ku_rum.backend.domain.department.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import ku_rum.backend.domain.department.domain.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    boolean existsByUserIdAndDepartmentId(Long userId, Long departmentId);

    Optional<UserDepartment> findByUserIdAndDepartmentId(Long userId, Long departmentId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM UserDepartment ud WHERE ud.user.id = :userId AND ud.department.id = :deptId")
    int deleteByUserIdAndDepartmentId(Long userId, Long deptId);
}
