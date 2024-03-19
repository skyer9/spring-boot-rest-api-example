package com.example.api.config;

import com.example.api.domain.Authority;
import com.example.api.domain.MyUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionAuthenticationProvider {
    public Authentication getAuthentication(MyUser myUser) {
        Collection<? extends GrantedAuthority> authorities = myUser
                .getAuthorities()
                .stream()
                .map(Authority::addPrefix)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(myUser.getUsername(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
