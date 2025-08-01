package ku_rum.backend.domain.place.domain.repository;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import ku_rum.backend.domain.place.domain.PlaceAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceAliasRepository extends JpaRepository<PlaceAlias, Long> {

    Optional<PlaceAlias> findPlaceAliasByName(@NotNull String name);
}
