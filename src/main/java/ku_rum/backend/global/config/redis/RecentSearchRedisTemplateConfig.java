package ku_rum.backend.global.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RecentSearchRedisTemplateConfig extends RedisConfig {

    @Bean
    @Qualifier("recentSearchRedisTemplate")
    public RedisTemplate<String, String> recentSearchRedisTemplate() {
        RedisTemplate<String, String> recentSearchRedisTemplate = new RedisTemplate<>();
        recentSearchRedisTemplate.setKeySerializer(new StringRedisSerializer());
        recentSearchRedisTemplate.setValueSerializer(new StringRedisSerializer());
        recentSearchRedisTemplate.setConnectionFactory(redisConnectionFactory(2));
        return recentSearchRedisTemplate;
    }
}
