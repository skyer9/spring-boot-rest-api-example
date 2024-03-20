package com.example.api.config;

import com.example.api.domain.*;
import com.example.api.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "MY_SESS";
    public static final String LOGIN_COOKIE_NAME = "MY_AUTH";
    // private final RedisService sessionStore;
    private final LoginCookieRepository loginCookieRepository;
    private final MyUserRepository myUserRepository;
    private final RedisSessionRepository redisSessionRepository;

    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        redisSessionRepository.save(RedisSession
                .builder()
                        .key(sessionId)
                        .value((MyUser) value)
                        .timeToLive((long) (15 * 60 * 1000))    // 15 minutes
                .build());
        // sessionStore.put(sessionId, value, (long) (15 * 60 * 1000));    // 15 minutes

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        System.out.println("1111111111111");
        System.out.println(sessionId);
        response.addCookie(cookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }
        System.out.println("1111111111111 222");
        System.out.println(cookie.getValue());
        Optional<RedisSession> redisSession = redisSessionRepository.findById(cookie.getValue());
        return redisSession.orElse(null);
    }

    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            // sessionStore.remove(sessionCookie.getValue());
            redisSessionRepository.deleteById(sessionCookie.getValue());
            sessionCookie.setMaxAge(-1);
        }
        Cookie loginCookie = findCookie(request, LOGIN_COOKIE_NAME);
        if (loginCookie != null) {
            // loginCookieRepository.deleteById(loginCookie.getValue());
            loginCookie.setMaxAge(-1);
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    public void createLoginCookie(MyUser myUser, HttpServletResponse response) {
        long now = (new Date()).getTime();
        while (true) {
            String loginCookie = UUID.randomUUID().toString();
            if (loginCookieRepository.findTop1ByCookieOrderByIdDesc(loginCookie).isEmpty()) {
                loginCookieRepository.save(LoginCookie
                        .builder()
                        .username(myUser.getUsername())
                        .cookie(loginCookie)
                        .createTime(now)
                        .expireTime(now + 86400 * 1000 * 14) // 14 day
                        .build());
                Cookie cookie = new Cookie(LOGIN_COOKIE_NAME, loginCookie);
                cookie.setMaxAge(86400 * 14); // 14 day
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.addCookie(cookie);
                break;
            }
        }
    }

    public Object getSessionByCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = findCookie(request, LOGIN_COOKIE_NAME);
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
