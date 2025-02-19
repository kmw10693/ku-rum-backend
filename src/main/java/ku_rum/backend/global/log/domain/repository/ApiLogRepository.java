package ku_rum.backend.global.log.domain.repository;

import ku_rum.backend.global.log.domain.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

}
