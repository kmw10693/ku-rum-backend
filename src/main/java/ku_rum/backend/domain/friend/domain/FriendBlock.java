package ku_rum.backend.domain.friend.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FriendBlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromUser_id", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toUser_id", nullable = false)
    private User toUser;

    public static FriendBlock of(User fromUser, User toUser) {
        return FriendBlock.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

}
