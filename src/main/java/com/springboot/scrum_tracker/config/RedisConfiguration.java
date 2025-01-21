package com.springboot.scrum_tracker.config;

import java.time.Duration;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@Configuration
public class RedisConfiguration implements CachingConfigurer{

	@Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
			
			@Override
			public void handleCachePutError(RuntimeException exception, org.springframework.cache.Cache cache, Object key,
					Object value) {
				log.error("Failure putting into cache: " + cache.getName() + ", exception: " + exception.toString());
				
			}
			
			@Override
			public void handleCacheGetError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
				log.error("Failure getting from cache: " + cache.getName() + ", exception: " + exception.toString());
				
			}
			
			@Override
			public void handleCacheEvictError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
				log.error("Failure evicting from cache: " + cache.getName() + ", exception: " + exception.toString());
				
			}
			
			@Override
			public void handleCacheClearError(RuntimeException exception, org.springframework.cache.Cache cache) {
				log.error("Failure clearing cache: " + cache.getName() + ", exception: " + exception.toString());
				
			}
		};
		
    }
	
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
		
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(600)).disableCachingNullValues()
				.serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
						
	}
}
