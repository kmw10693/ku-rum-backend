package ku_rum.backend.domain.friend.domain.repository;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
    @EntityGraph(attributePaths = {"fromUser", "toUser"})
    Optional<Friend> findFirstByFromUserAndToUserAndStatus(User fromUser, User toUser, FriendStatus status);
}