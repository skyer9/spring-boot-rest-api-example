package com.example.api.service;

import com.example.api.domain.MyUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public void putSession(String key, MyUser value, Long expiredTime) {
        try {
            String jsonString = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonString, expiredTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public MyUser getSession(String key) {
        try {
            String jsonString = (String) redisTemplate.opsForValue().get(key);
            return mapper.readValue(jsonString, MyUser.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
