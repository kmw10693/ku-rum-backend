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
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
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

    @Enumerated(EnumType.STRING)
    private AgreementStatus agreementStatus;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void deactivate() { this.active = false; }

    @Builder
    private User(String loginId, String email, String nickname, String password, String studentId, Department department, AgreementStatus agreementStatus) {
        this.loginId = loginId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.studentId = studentId;
        this.department = department;
        this.roles.add(USER.getRole());
        this.active = true;
        this.agreementStatus = agreementStatus;
    }

    public static User of(String loginId, String email, String nickname, String password, String studentId, Department department, AgreementStatus agreementStatus) {
        return User.builder()
                .loginId(loginId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .studentId(studentId)
                .department(department)
                .agreementStatus(agreementStatus)
                .build();
    }
}
