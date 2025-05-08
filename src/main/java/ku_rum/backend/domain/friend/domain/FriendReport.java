package ku_rum.backend.domain.friend.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.user.domain.User;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FriendReport {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "fromUser_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    @JoinColumn(name = "toUser_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;

    private String reason;

    public static FriendReport of(User fromUser, User toUser, String reason) {
        return FriendReport.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .reason(reason)
                .build();
    }
}
