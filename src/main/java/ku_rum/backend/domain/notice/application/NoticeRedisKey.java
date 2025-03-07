package ku_rum.backend.domain.notice.application;

import lombok.Getter;

@Getter
public enum NoticeRedisKey {
    NOTICE_REDIS_KEY_PREFIX("konkuk:notice:");

    private final String prefix;

    NoticeRedisKey(String prefix) {
        this.prefix = prefix;
    }
}
