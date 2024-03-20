package com.example.api.web;

import com.example.api.common.ResponseDto;
import com.example.api.config.SessionManager;
import com.example.api.domain.MyUser;
import com.example.api.service.MyUserService;
import com.example.api.service.dto.LoginDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {
    private final MyUserService myUserService;
    private final SessionManager sessionManager;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        MyUser myUser = myUserService.login(request, username, password);
        sessionManager.createSession(myUser, response);
        sessionManager.createLoginCookie(myUser, response);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.ACCEPTED, "OK"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.deleteSession(request, response);
        sessionManager.deleteLoginCookie(request, response);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.ACCEPTED, "OK"));
    }
}
