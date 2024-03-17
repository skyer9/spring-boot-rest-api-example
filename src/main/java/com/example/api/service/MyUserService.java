package com.example.api.service;

import com.example.api.common.SecurityUtil;
import com.example.api.config.JwtTokenProvider;
import com.example.api.domain.*;
import com.example.api.service.dto.MyUserDto;
import com.example.api.service.dto.TokenDto;
import com.example.api.web.advice.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MyUserService {
    private final MyUserRepository myUserRepository;
    private final LoginLogService loginLogService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional(noRollbackFor = UserNotFoundException.class)
    public TokenDto login(HttpServletRequest request, String username, String password) {
        if (loginLogService.isBlocked(username)) {
            throw new RuntimeException("Too many login fail, wait please");
        }

        MyUser myUser = getUserWithAuthorities(username);
        if (!passwordEncoder.matches(password, myUser.getPassword())) {
            loginLogService.loginFailed(username, SecurityUtil.getClientIpAddress(request));
            throw new UserNotFoundException("username or password is incorrect");
        }

        loginLogService.loginSucceeded(username, SecurityUtil.getClientIpAddress(request));
        return jwtTokenProvider.generateToken(myUser);
    }

    @Transactional
    public TokenDto reissue(RefreshToken refreshToken) {
        MyUser myUser = getUserWithAuthorities(refreshToken.getUsername());
        return jwtTokenProvider.reissueToken(myUser, refreshToken);
    }

    @Transactional
    public MyUser createAdminUser(MyUserDto myUserDto) {
        if (myUserRepository.count() > 0) {
            throw new RuntimeException("User account must be zero");
        }

        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.builder().authorityName("ADMIN").build());
        authorities.add(Authority.builder().authorityName("USER").build());

        MyUser myUser = MyUser.builder()
                .username(myUserDto.getUsername())
                .password(passwordEncoder.encode(myUserDto.getPassword()))
                .nickname(myUserDto.getNickname())
                .authorities(authorities)
                .activated(true)
                .build();

        return myUserRepository.save(myUser);
    }

    @Transactional
    public MyUser signup(MyUserDto myUserDto) {
        if (myUserRepository.findOneWithAuthoritiesByUsername(myUserDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("Unavailable username");
        }

        Authority authority = Authority.builder()
                .authorityName("USER")
                .build();

        MyUser myUser = MyUser.builder()
                .username(myUserDto.getUsername())
                .password(passwordEncoder.encode(myUserDto.getPassword()))
                .nickname(myUserDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

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
    public MyUser getMyUserWithAuthorities() {
        Optional<MyUser> myUser = SecurityUtil
                .getCurrentUsername()
                .flatMap(myUserRepository::findOneWithAuthoritiesByUsername);
        if (myUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return myUser.get();
    }
}
