package ku_rum.backend.domain.auth.application;

import ku_rum.backend.domain.auth.dto.request.ReissueRequest;
import ku_rum.backend.global.config.redis.RedisUtil;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TokenBlacklistService {
    private final RedisUtil redisUtil;

    public boolean validateRefreshTokenInRedis(ReissueRequest reissueRequest, CustomUserDetails principal) {
        Long userId = principal.getUserId();
        String redisRefreshToken = redisUtil.getRedisData(String.valueOf(userId));

        if (redisRefreshToken != null && redisRefreshToken.equals(reissueRequest.refreshToken())) {
            redisUtil.deleteRedisData(String.valueOf(userId));
            return true;
        }
        return false;
    }

    public void setBlackListInRedis(String token, long expiredAccessTokenTime, Long userId) {
        redisUtil.setBlackList(token, "logout", Duration.ofMillis(expiredAccessTokenTime));
        redisUtil.deleteRedisData(String.valueOf(userId));
    }

}
