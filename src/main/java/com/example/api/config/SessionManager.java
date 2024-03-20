package com.example.api.config;

import com.example.api.common.CookieUtil;
import com.example.api.domain.LoginCookie;
import com.example.api.domain.LoginCookieRepository;
import com.example.api.domain.MyUser;
import com.example.api.domain.MyUserRepository;
import com.example.api.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "MY_SESS";
    public static final String LOGIN_COOKIE_NAME = "MY_AUTH";
    private final RedisService redisService;
    private final LoginCookieRepository loginCookieRepository;
    private final MyUserRepository myUserRepository;

    @Transactional
    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        int maxAge = 15 * 60;
        redisService.putSession(sessionId, value, (long) (maxAge * 1000));    // 15 minutes
        CookieUtil.setCookie(response, SESSION_COOKIE_NAME, sessionId, maxAge);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = CookieUtil.getCookie(request, SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }
        Optional<MyUser> o = redisService.getSession(cookie.getValue(), MyUser.class);
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

    @Transactional
    public void createLoginCookie(MyUser myUser, HttpServletResponse response) {
        long now = (new Date()).getTime();
        String loginCookie = UUID.randomUUID().toString();
        int maxAge = 86400 * 14;    // 14 day
        CookieUtil.setCookie(response, LOGIN_COOKIE_NAME, loginCookie, maxAge);

        loginCookieRepository.save(LoginCookie
                .builder()
                .username(myUser.getUsername())
                .cookie(loginCookie)
                .createTime(now)
                .expireTime(now + (maxAge * 1000)) // 14 day
                .build());
    }

    @Transactional
    public void deleteLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, LOGIN_COOKIE_NAME);
        if (cookie == null) {
            return;
        }
        Optional<LoginCookie> loginCookie = loginCookieRepository.findTop1ByCookieOrderByIdDesc(cookie.getValue());
        loginCookie.ifPresent(value -> loginCookieRepository.deleteById(value.getId()));
        CookieUtil.deleteCookie(request, response, LOGIN_COOKIE_NAME);
    }

    @Transactional
    public Object getSessionByCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, LOGIN_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }

        Optional<LoginCookie> loginCookie = loginCookieRepository.findTop1ByCookieOrderByIdDesc(cookie.getValue());
        if (loginCookie.isEmpty()) {
            return null;
        }

        Optional<MyUser> myUser = myUserRepository.findOneWithAuthoritiesByUsername(loginCookie.get().getUsername());
        if (myUser.isEmpty()) {
            return null;
        }

        createSession(myUser, response);

        return myUser.get();
    }


}
