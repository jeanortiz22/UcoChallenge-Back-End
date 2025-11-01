package co.edu.uco.messageservice.infrastructure;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(15)); // TTL por defecto

        return RedisCacheManager.builder(cf)
                .cacheDefaults(base)
                .withCacheConfiguration("messages", base.entryTtl(Duration.ofMinutes(60)))        // positivos: 60m
                .withCacheConfiguration("messagesAll", base.entryTtl(Duration.ofMinutes(30)))     // catálogo completo: 30m
                .withCacheConfiguration("messagesNotFound", base.entryTtl(Duration.ofMinutes(5))) // negativos: 5m
                .build();
    }
}
