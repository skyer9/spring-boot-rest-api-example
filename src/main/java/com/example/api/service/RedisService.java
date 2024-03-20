package com.example.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public void putSession(String key, Object value, Long expiredTime) {
        try {
            String jsonString = mapper.writeValueAsString(value);
            System.out.println("111111111111111");
            System.out.println(key);
            System.out.println(jsonString);
            redisTemplate.opsForValue().set(key, jsonString, expiredTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public <T> Optional<T> getSession(String key, Class<T> valueType) {
        try {
            String jsonString = (String) redisTemplate.opsForValue().get(key);
            System.out.println("2222222222");
            System.out.println(key);
            System.out.println(jsonString);
            if (StringUtils.hasText(jsonString)) {
                return Optional.ofNullable(mapper.readValue(jsonString, valueType));
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
