package ku_rum.backend.domain.alim.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Builder
    private Alim(String keyword, User user) {
        this.keyword = keyword;
        this.user = user;
    }

    public static Alim of(String keyword, User user) {
        return Alim.builder()
                .keyword(keyword)
                .user(user)
                .build();
    }

}
