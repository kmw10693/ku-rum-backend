package ku_rum.backend.global.dataInit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            jdbcTemplate.execute(
                    "ALTER TABLE building ADD FULLTEXT INDEX name_fulltext_index (name) WITH PARSER ngram"
            );
            jdbcTemplate.execute(
                    "ALTER TABLE category ADD FULLTEXT INDEX name_fulltext_index (name) WITH PARSER ngram"
            );
            log.info("✅ FULLTEXT INDEX 추가 완료");
        } catch (Exception e) {
            log.error("❌ FULLTEXT INDEX 추가 실패: {}", e.getMessage(), e);
        }
    }
}

