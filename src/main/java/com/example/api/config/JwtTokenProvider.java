package com.example.api.config;

import com.example.api.domain.Authority;
import com.example.api.domain.MyUser;
import com.example.api.domain.RefreshToken;
import com.example.api.domain.RefreshTokenRepository;
import com.example.api.service.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {
    private Key key;

    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;
    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto generateToken(MyUser myUser) {
        long now = (new Date()).getTime();
        String authorities = getAuthorities(myUser);
        String accessToken = generateAccessToken(myUser.getUsername(), authorities, now);
        String refreshToken = generateRefreshToken(myUser.getUsername(), now);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto reissueToken(MyUser myUser, RefreshToken refreshToken) {
        validateToken(refreshToken.getToken());

        long now = (new Date()).getTime();
        String authorities = getAuthorities(myUser);
        String accessToken = generateAccessToken(refreshToken.getUsername(), authorities, now);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty");
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
        return true;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String generateAccessToken(String username, String authorities, long now) {
        Date accessTokenExpiresIn = new Date(now + accessTokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private String generateRefreshToken(String username, long now) {
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(now + refreshTokenValidityInMilliseconds))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        Optional<RefreshToken> saved = refreshTokenRepository.findByToken(refreshToken);
        if (saved.isEmpty()) {
            refreshTokenRepository.save(RefreshToken
                    .builder()
                    .token(refreshToken)
                    .expiryDate(now + refreshTokenValidityInMilliseconds)
                    .username(username)
                    .build());
        }

        return refreshToken;
    }

    private String getAuthorities(MyUser myUser) {
        return myUser
                .getAuthorities()
                .stream()
                .map(Authority::addPrefix)
                .collect(Collectors.joining(","));
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
