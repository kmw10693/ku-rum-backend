package ku_rum.backend.domain.user.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.oauth.domain.OAuth2MemberInfo;
import ku_rum.backend.domain.oauth.domain.ProviderType;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

import static ku_rum.backend.domain.user.domain.UserRole.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId;

    @Column(length = 20, unique = true)
    private String loginId;

    @Column(unique = true)
    private String email;

    @Column(length = 50)
    private String nickname;

    @Column(length = 128)
    private String password;

    @Column(length = 15)
    private String studentId;

    private String imageUrl;

    @ElementCollection
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private AgreementStatus agreementStatus;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    @Builder
    private User(String loginId, String oauthId, String email, String nickname, String password, String studentId, AgreementStatus agreementStatus, ProviderType providerType) {
        this.loginId = loginId;
        this.oauthId = oauthId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.studentId = studentId;
        this.roles.add(USER.getRole());
        this.agreementStatus = agreementStatus;
        this.providerType = providerType;
    }

    public static User of(String loginId, String email, String nickname, String password, String studentId, Department department, AgreementStatus agreementStatus, ProviderType providerType) {
        return User.builder()
                .loginId(loginId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .studentId(studentId)
                .agreementStatus(agreementStatus)
                .providerType(providerType)
                .build();
    }

    public static User createMemberWithOAuthInfo(OAuth2MemberInfo memberInfo, ProviderType providerType) {
        return User.builder()
                .oauthId(memberInfo.getId())
                .nickname(memberInfo.getName())
                .email(memberInfo.getEmail())
                .providerType(providerType) // enum 변환
                .build();
    }
}
