package ku_rum.backend.domain.common.mail.domain.repository;

import ku_rum.backend.domain.common.mail.domain.MailAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MailAuthRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(MailAuth mailAuth, Duration expiration) {
        redisTemplate.opsForValue().set(
                generateKey(mailAuth.getEmail()),
                mailAuth.getAuthCode(),
                expiration
        );
    }

    public Optional<MailAuth> findByEmail(String email) {
        String key = generateKey(email);
        String authCode = redisTemplate.opsForValue().get(key);
        if (authCode == null) {
            return Optional.empty();
        }
        return Optional.of(MailAuth.of(email, authCode));
    }

    public void deleteByEmail(String email) {
        String key = generateKey(email);
        redisTemplate.delete(key);
    }

    private String generateKey(String email) {
        return "MAIL_AUTH:" + email;
    }
}
