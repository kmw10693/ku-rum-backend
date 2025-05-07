package ku_rum.backend.domain.department.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Builder
    private UserDepartment(User user, Department department) {
        this.user = user;
        this.department = department;
    }

    public static UserDepartment of(User user, Department department) {
        return UserDepartment.builder()
                .user(user)
                .department(department)
                .build();
    }

}
