package ku_rum.backend.domain.bookmark.domain.repository;

import ku_rum.backend.domain.bookmark.domain.Bookmark;
import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.user JOIN FETCH b.notice WHERE b.user = :user")
    List<Bookmark> findByUser(User user);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.user JOIN FETCH b.notice WHERE b.user = :user AND b.notice = :notice")
    Optional<Bookmark> findByUserAndNotice(User user, Notice notice);

    boolean existsByUserAndNotice(User user, Notice notice);
}
