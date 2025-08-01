package ku_rum.backend.domain.rank.domain.repository;

import java.util.List;
import java.util.Optional;
import ku_rum.backend.domain.place.domain.SubPlace;
import ku_rum.backend.domain.rank.domain.PlaceRank;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRankRepository extends JpaRepository<PlaceRank, Long> {

    List<PlaceRank> findTop3ByUserOrderByCountDesc(User user);

    Optional<PlaceRank> findByUserAndSubPlace(User user, SubPlace subPlace);
}
