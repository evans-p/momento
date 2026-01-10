package gr.evansp.momento.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Slf4j
@Configuration
public class RedisConfiguration implements CachingConfigurer {

  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    return new CacheErrorHandler() {

      @Override
      public void handleCacheGetError(
          @NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.error("Cache GET error for key: {} in cache: {}", key, cache.getName(), exception);
      }

      @Override
      public void handleCachePutError(
          @NonNull RuntimeException exception,
          @NonNull Cache cache,
          @NonNull Object key,
          Object value) {
        log.error("Cache PUT error for key: {} in cache: {}", key, cache.getName(), exception);
      }

      @Override
      public void handleCacheEvictError(
          @NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.error("Cache EVICT error for key: {} in cache: {}", key, cache.getName(), exception);
      }

      @Override
      public void handleCacheClearError(@NonNull RuntimeException exception, @NonNull Cache cache) {
        log.error("Cache CLEAR error in cache: {}", cache.getName(), exception);
      }
    };
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    Hibernate6Module hibernate6Module = new Hibernate6Module();
    hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
    objectMapper.registerModule(hibernate6Module);

    GenericJackson2JsonRedisSerializer serializer =
        new GenericJackson2JsonRedisSerializer(objectMapper);

    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(60))
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(serializer));
  }
}
