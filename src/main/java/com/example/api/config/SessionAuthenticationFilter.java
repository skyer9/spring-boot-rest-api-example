package com.example.api.config;

import com.example.api.domain.MyUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class SessionAuthenticationFilter extends GenericFilterBean {
    private final SessionManager sessionManager;
    private final SessionAuthenticationProvider sessionAuthenticationProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Object o = sessionManager.getSession((HttpServletRequest) request);
        if (o instanceof MyUser myUser) {
            Authentication authentication = sessionAuthenticationProvider.getAuthentication(myUser);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
