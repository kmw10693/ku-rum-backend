package ku_rum.backend.global.dataInit;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final EntityManager entityManager;

    @PostConstruct
    public void init() {
        entityManager.createNativeQuery(
                "ALTER TABLE building ADD FULLTEXT INDEX name_fulltext_index (name) WITH PARSER ngram"
        ).executeUpdate();
    }
}

