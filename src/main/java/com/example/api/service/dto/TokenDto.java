package com.example.api.service.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
