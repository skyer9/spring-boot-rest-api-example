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

    public void putData(String key, Object value, Long expiredTime) {
        try {
            String jsonString = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonString, expiredTime, TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid json format: ", e);
        }
    }

    public <T> Optional<T> getData(String key, Class<T> valueType) {
        try {
            String jsonString = (String) redisTemplate.opsForValue().get(key);
            if (StringUtils.hasText(jsonString)) {
                return Optional.ofNullable(mapper.readValue(jsonString, valueType));
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid json format: ", e);
        }
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
