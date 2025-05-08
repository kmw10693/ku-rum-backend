package ku_rum.backend.domain.friend.domain.repository;

import ku_rum.backend.domain.friend.domain.FriendBlock;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendBlockRepository extends JpaRepository<FriendBlock, Long> {
    boolean existsByFromUserAndToUser(User fromUser, User toUser);
}
