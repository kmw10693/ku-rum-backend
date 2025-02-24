package ku_rum.backend.domain.user.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static ku_rum.backend.domain.user.domain.UserRole.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET active = true WHERE id = ?")
@Where(clause = "active = true")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(length = 128)
    private String password;

    @Column(nullable = false, length = 15)
    private String studentId;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ElementCollection
    private List<String> roles = new ArrayList<>();

    private boolean active;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Builder
    private User(String loginId, String email, String nickname, String password, String studentId, Department department) {
        this.loginId = loginId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.studentId = studentId;
        this.department = department;
        this.roles.add(USER.getRole());
        this.active = true;
    }

    public static User of(String loginId, String email, String nickname, String password, String studentId, Department department) {
        return User.builder()
                .loginId(loginId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .studentId(studentId)
                .department(department)
                .build();
    }
}
