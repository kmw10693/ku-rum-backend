package ku_rum.backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {
  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  @Primary
  public LettuceConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
  }

  //DB 1 사용
  @Bean
  public LettuceConnectionFactory redisConnectionFactoryForDB1() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
    config.setDatabase(1);
    return new LettuceConnectionFactory(config);
  }

  @Bean
  public RedisTemplate<String, Integer> redisTemplate() {
    RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, String> urlRedisTemplate() {
    RedisTemplate<String, String> urlRedisTemplate = new RedisTemplate<>();
    urlRedisTemplate.setKeySerializer(new StringRedisSerializer());
    urlRedisTemplate.setValueSerializer(new StringRedisSerializer());
    urlRedisTemplate.setConnectionFactory(redisConnectionFactory());
    return urlRedisTemplate;
  }

  @Bean
  public RedisTemplate<String, String> recentSearchRedisTemplate() {
    RedisTemplate<String, String> recentSearchRedisTemplate = new RedisTemplate<>();
    recentSearchRedisTemplate.setKeySerializer(new StringRedisSerializer());
    recentSearchRedisTemplate.setValueSerializer(new StringRedisSerializer());
    recentSearchRedisTemplate.setConnectionFactory(redisConnectionFactoryForDB1()); //DB 1 사용
    return recentSearchRedisTemplate;
  }


}