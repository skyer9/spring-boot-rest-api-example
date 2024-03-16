package com.example.api.web;

import com.example.api.domain.MyUser;
import com.example.api.service.MyUserService;
import com.example.api.service.dto.MyUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyUserController {
    private final MyUserService myUserService;

    @PostMapping("/createAdminUser")
    public ResponseEntity<MyUser> createAdminUser(
            @Valid @RequestBody MyUserDto userDto
    ) {
        return ResponseEntity.ok(myUserService.createAdminUser(userDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<MyUser> signup(
            @Valid @RequestBody MyUserDto userDto
    ) {
        return ResponseEntity.ok(myUserService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MyUser> getMyUserInfo() {
        return ResponseEntity.ok(myUserService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MyUser> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(myUserService.getUserWithAuthorities(username));
    }
}
