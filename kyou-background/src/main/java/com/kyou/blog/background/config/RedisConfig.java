package com.kyou.blog.background.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyou.blog.common.meta.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author CC
 * time 2023-07-08
 * description 对于Redis的配置，并启用了spring-cache
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig  extends CachingConfigurerSupport{
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory){
        RedisSerializer<String> string = RedisSerializer.string();
        Jackson2JsonRedisSerializer js = new Jackson2JsonRedisSerializer<>(Object.class);
        //解决缓存转换异常
        JacksonObjectMapper jom = new JacksonObjectMapper();
        jom.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        jom.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        js.setObjectMapper(jom);
        //配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(string))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(js))
                .disableCachingNullValues();
        RedisCacheManager build = RedisCacheManager.builder(factory).cacheDefaults(config).build();
        return build;

    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        log.info("开始配置redis");
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化工具,相当于RedisSerializer.json()
        Jackson2JsonRedisSerializer js = new Jackson2JsonRedisSerializer<>(Object.class);
        JacksonObjectMapper jom = new JacksonObjectMapper();
        jom.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jom.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        js.setObjectMapper(jom);
        //key和Hashkey采用string
        RedisSerializer<String> string = RedisSerializer.string();
        redisTemplate.setKeySerializer(string);
        redisTemplate.setHashKeySerializer(string);
        //value和hashValue采用JSON序列化，在取值时，使用new ObjectMapper().readValue(obj,type)
        redisTemplate.setValueSerializer(js);
        redisTemplate.setHashValueSerializer(js);
        return redisTemplate;
    }

}
