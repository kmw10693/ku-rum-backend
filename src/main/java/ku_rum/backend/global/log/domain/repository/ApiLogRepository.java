package ku_rum.backend.global.log.domain.repository;

import ku_rum.backend.global.log.domain.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE ApiLog a SET a.responseStatus = :status, a.response = :response, a.responseTime = CURRENT_TIMESTAMP WHERE a.seq = :seq")
    void updateResponse(Long seq, int status, String response);
}
