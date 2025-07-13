package ku_rum.backend.global.dataInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FulltextIndexInitializer {

    private final JdbcTemplate jdbcTemplate;

    public FulltextIndexInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void insert_fulltext_index(String tableName, String indexName, String columnName) {
        if (!isIndexExists(tableName, indexName)) {
            String sql = String.format(
                    "ALTER TABLE %s ADD FULLTEXT INDEX %s (" + columnName + ") WITH PARSER ngram",
                    tableName,
                    indexName
            );
            jdbcTemplate.execute(sql);
            log.info("✅ FULLTEXT INDEX 추가됨: 테이블={}, 인덱스={}", tableName, indexName);
        }
    }

    private boolean isIndexExists(String tableName, String indexName) {
        String sql = "SELECT COUNT(*) FROM information_schema.statistics " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = ? " +
                "AND index_name = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, tableName, indexName) > 0;
    }
}

