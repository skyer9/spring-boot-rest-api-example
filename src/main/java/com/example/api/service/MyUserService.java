package com.example.api.service;

import com.example.api.common.SecurityUtil;
import com.example.api.domain.Authority;
import com.example.api.domain.MyUser;
import com.example.api.domain.MyUserRepository;
import com.example.api.service.dto.MyUserDto;
import com.example.api.web.advice.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MyUserService {
    private final MyUserRepository myUserRepository;
    private final LoginLogService loginLogService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(noRollbackFor = UserNotFoundException.class)
    public MyUser login(HttpServletRequest request, String username, String password) {
        if (loginLogService.isBlocked(username)) {
            throw new RuntimeException("Too many login fail, wait please");
        }

        MyUser myUser = getUserWithAuthorities(username);
        if (!passwordEncoder.matches(password, myUser.getPassword())) {
            loginLogService.loginFailed(username, SecurityUtil.getClientIpAddress(request));
            throw new UserNotFoundException("username or password is incorrect");
        }

        loginLogService.loginSucceeded(username, SecurityUtil.getClientIpAddress(request));
        return myUser;
    }

    @Transactional
    public MyUser createAdminUser(MyUserDto myUserDto) {
        if (myUserRepository.count() > 0) {
            throw new RuntimeException("User account must be zero");
        }

        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.builder().authorityName("ADMIN").build());
        authorities.add(Authority.builder().authorityName("USER").build());
        MyUser myUser = createUserEntity(myUserDto, authorities);

        return myUserRepository.save(myUser);
    }

    @Transactional
    public MyUser signup(MyUserDto myUserDto) {
        if (myUserRepository.findOneWithAuthoritiesByUsername(myUserDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("Unavailable username");
        }

        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.builder().authorityName("USER").build());
        MyUser myUser = createUserEntity(myUserDto, authorities);

        return myUserRepository.save(myUser);
    }

    @Transactional(readOnly = true)
    public MyUser getUserWithAuthorities(String username) {
        Optional<MyUser> myUser = myUserRepository.findOneWithAuthoritiesByUsername(username);
        if (myUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return myUser.get();
    }

    @Transactional(readOnly = true)
    public MyUser getUserWithAuthoritiesFromSession() {
        Optional<MyUser> myUser = SecurityUtil
                .getCurrentUsername()
                .flatMap(myUserRepository::findOneWithAuthoritiesByUsername);
        if (myUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return myUser.get();
    }

    private MyUser createUserEntity(MyUserDto myUserDto, Set<Authority> authorities) {
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return MyUser.builder()
                .username(myUserDto.getUsername())
                .password(passwordEncoder.encode(myUserDto.getPassword()))
                .nickname(myUserDto.getNickname())
                .lastLoginDate(now.format(new Date()))
                .authorities(authorities)
                .activated(true)
                .build();
    }
}
