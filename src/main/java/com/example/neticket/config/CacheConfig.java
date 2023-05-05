package com.example.neticket.config;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Bean
  public CacheManager localCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("localCache");
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.HOURS)
        .maximumSize(20));
    return cacheManager;
  }

  @Bean
  public CacheManager redisCacheManager() {
    RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(redisConnectionFactory());
    return builder.build();
  }

  @Primary
  @Bean
  @Override
  public CacheManager cacheManager() {
    return new CompositeCacheManager(localCacheManager(), redisCacheManager());
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .commandTimeout(Duration.ofMillis(100)) // 타임아웃 설정
        .build();

    RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(host, port);

    return new LettuceConnectionFactory(serverConfig, clientConfig);
  }

  //  LeftSeats에 사용하는 redisTemplate
  @Bean(name = "leftSeat")
  public RedisTemplate<String, Integer> redisTemplate() {
    RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
    return redisTemplate;
  }

  // 공연 데이터 캐시 redisTemplate
  @Bean(name = "eventDto")
  public RedisTemplate<String, DetailEventResponseDto> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    Jackson2JsonRedisSerializer<DetailEventResponseDto> serializer = new Jackson2JsonRedisSerializer<>(
        DetailEventResponseDto.class);
    serializer.setObjectMapper(objectMapper);

    RedisTemplate<String, DetailEventResponseDto> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(serializer);

    return redisTemplate;
  }

}
