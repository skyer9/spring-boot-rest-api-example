package com.example.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    Optional<LoginLog> findTop1ByUsernameOrderByIdDesc(String username);
}
