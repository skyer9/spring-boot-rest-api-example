package com.example.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "login_cookie")
public class LoginCookie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128)
    private String username;

    @Column(length = 256)
    private String cookie;

    private Long createTime;
    private Long expireTime;
}
