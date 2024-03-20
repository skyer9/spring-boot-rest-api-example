package com.example.api.domain;

import org.springframework.data.repository.CrudRepository;

public interface RedisSessionRepository extends CrudRepository<RedisSession, String> {
}
