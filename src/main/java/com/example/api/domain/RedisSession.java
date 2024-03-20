package com.example.api.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash
public class RedisSession {
    @Id
    private String key;

    private MyUser value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long timeToLive;
}
