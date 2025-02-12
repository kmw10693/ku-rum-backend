package ku_rum.backend.domain.friend.domain;

import jakarta.persistence.*;

import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.type.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromUser_id", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toUser_id", nullable = false)
    private User toUser;

    private FriendStatus status;

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    @Builder
    private Friend(User fromUser, User toUser, FriendStatus status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
    }

    public static Friend of(User fromUser, User toUser, FriendStatus status) {
        return Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(status)
                .build();
    }
}
