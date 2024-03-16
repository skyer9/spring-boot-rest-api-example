package com.example.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, String> {
    Optional<MyUser> findByUsername(String username);

    Optional<MyUser> findOneWithAuthoritiesByUsername(String username);
}
