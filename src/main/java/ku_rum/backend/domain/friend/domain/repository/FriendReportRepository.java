package ku_rum.backend.domain.friend.domain.repository;

import ku_rum.backend.domain.friend.domain.FriendReport;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendReportRepository extends JpaRepository<FriendReport, Long> {
    boolean existsByFromUserAndToUser(User fromUser, User toUser);
}
