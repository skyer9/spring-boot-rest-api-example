package com.example.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "login_log")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 128)
    private String username;

    private Long lastLoginSuccessTime;
    private Long lastLoginFailTime;
    private Long loginFailCount;

    private String clientIp;
}
