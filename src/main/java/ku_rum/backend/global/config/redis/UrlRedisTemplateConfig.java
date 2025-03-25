package ku_rum.backend.global.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class UrlRedisTemplateConfig extends RedisConfig {

    @Bean
    @Qualifier("urlRedisTemplate")
    @Primary
    public RedisTemplate<String, String> urlRedisTemplate() {
        RedisTemplate<String, String> urlRedisTemplate = new RedisTemplate<>();
        urlRedisTemplate.setKeySerializer(new StringRedisSerializer());
        urlRedisTemplate.setValueSerializer(new StringRedisSerializer());
        urlRedisTemplate.setConnectionFactory(redisConnectionFactory(0));
        return urlRedisTemplate;
    }
}
