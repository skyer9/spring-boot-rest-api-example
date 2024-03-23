package com.example.api.config;

import com.example.api.common.CookieUtil;
import com.example.api.domain.LoginCookie;
import com.example.api.domain.LoginCookieRepository;
import com.example.api.domain.MyUser;
import com.example.api.domain.MyUserRepository;
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
public class CookieManager {
    private final LoginCookieRepository loginCookieRepository;
    private final MyUserRepository myUserRepository;
    private final SessionManager sessionManager;

    public MyUser getSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, SessionManager.LOGIN_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }

        Optional<LoginCookie> loginCookie = loginCookieRepository.findTop1ByCookieOrderByIdDesc(cookie.getValue());
        if (loginCookie.isEmpty()) {
            return null;
        }

        Optional<MyUser> o = myUserRepository.findOneWithAuthoritiesByUsername(loginCookie.get().getUsername());
        if (o.isEmpty()) {
            return null;
        }

        MyUser myUser = o.get();
        sessionManager.createSession(response, myUser);

        return myUser;
    }

    @Transactional
    public void createLoginCookie(MyUser myUser, HttpServletResponse response) {
        long now = (new Date()).getTime();
        String loginCookie = UUID.randomUUID().toString();
        int maxAge = 86400 * 14;    // 14 day
        CookieUtil.setCookie(response, SessionManager.LOGIN_COOKIE_NAME, loginCookie, maxAge);

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
        Cookie cookie = CookieUtil.getCookie(request, SessionManager.LOGIN_COOKIE_NAME);
        if (cookie == null) {
            return;
        }
        Optional<LoginCookie> loginCookie = loginCookieRepository.findTop1ByCookieOrderByIdDesc(cookie.getValue());
        loginCookie.ifPresent(value -> loginCookieRepository.deleteById(value.getId()));
        CookieUtil.deleteCookie(request, response, SessionManager.LOGIN_COOKIE_NAME);
    }
}
