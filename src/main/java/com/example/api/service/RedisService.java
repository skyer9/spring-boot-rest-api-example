package com.example.api.service;

import com.example.api.domain.MyUser;
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

    public void put(String key, Object value, Long expiredTime) {
        try {
            redisTemplate.opsForValue().set(key, mapper.writeValueAsString(value), expiredTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public <T> Optional<T> get(String key, Class<T> classType) {
        String jsonData = (String) redisTemplate.opsForValue().get(key);

        try {
            if (StringUtils.hasText(jsonData)) {
                return Optional.ofNullable(mapper.readValue(jsonData, classType));
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
