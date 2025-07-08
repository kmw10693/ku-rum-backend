package ku_rum.backend.domain.place.domain.repository;

import java.util.Optional;
import ku_rum.backend.domain.place.domain.Position;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

    boolean existsPositionByUser(User user);

    Optional<Position> findPositionByUser(User user);

    void deleteByUser(User user);
}
