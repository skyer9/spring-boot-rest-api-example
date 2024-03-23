package com.example.api.config;

import com.example.api.common.CookieUtil;
import com.example.api.domain.MyUser;
import com.example.api.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "MY_SESS";
    public static final String LOGIN_COOKIE_NAME = "MY_AUTH";
    private final RedisService redisService;

    @Transactional
    public void createSession(HttpServletResponse response, MyUser myUser) {
        String sessionId = UUID.randomUUID().toString();
        int maxAge = 15 * 60;
        redisService.putData(sessionId, myUser, (long) (maxAge * 1000));    // 15 minutes
        CookieUtil.setCookie(response, SESSION_COOKIE_NAME, sessionId, maxAge);
    }

    public MyUser getSession(HttpServletRequest request) {
        Cookie cookie = CookieUtil.getCookie(request, SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }
        Optional<MyUser> o = redisService.getData(cookie.getValue(), MyUser.class);
        return o.orElse(null);
    }

    @Transactional
    public void deleteSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, SESSION_COOKIE_NAME);
        if (cookie == null) {
            return;
        }
        redisService.remove(cookie.getValue());
        CookieUtil.deleteCookie(request, response, cookie.getName());
    }
}
