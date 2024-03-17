package com.example.api.web;

import com.example.api.config.JwtTokenProvider;
import com.example.api.domain.RefreshToken;
import com.example.api.service.MyUserService;
import com.example.api.service.RefreshTokenService;
import com.example.api.service.dto.LoginDto;
import com.example.api.service.dto.RefreshTokenRequestDTO;
import com.example.api.service.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final MyUserService myUserService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(HttpServletRequest request, @RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        TokenDto tokenDto = myUserService.login(request, username, password);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtTokenProvider.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody RefreshTokenRequestDTO requestDTO) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(requestDTO.getToken());
        if (refreshToken.isEmpty()) {
            throw new RuntimeException("Token not found");
        }
        TokenDto tokenDto = myUserService.reissue(refreshToken.get());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtTokenProvider.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }
}
