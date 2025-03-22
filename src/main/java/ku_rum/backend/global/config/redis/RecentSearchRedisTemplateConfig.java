package ku_rum.backend.global.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RecentSearchRedisTemplateConfig extends RedisConfig {

    @Bean(name = "recentSearchRedisTemplate")
    public RedisTemplate<String, String> recentSearchRedisTemplate() {
        RedisConnectionFactory redisConnectionFactory = redisConnectionFactory(2); // DB 인덱스 2 사용

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
