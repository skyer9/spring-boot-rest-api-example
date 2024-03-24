package com.example.api.config;

import com.example.api.domain.MyUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CookieAuthenticationFilter extends GenericFilterBean {
    private final CookieManager cookieManager;
    private final SessionAuthenticationProvider sessionAuthenticationProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Object o = cookieManager.getSession((HttpServletRequest) request, (HttpServletResponse) response);
            if (o instanceof MyUser myUser) {
                Authentication authentication = sessionAuthenticationProvider.getAuthentication(myUser);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
