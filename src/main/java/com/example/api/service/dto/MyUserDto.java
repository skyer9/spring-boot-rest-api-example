package com.example.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUserDto {
    @NotNull
    @Size(min = 3, max = 128)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 256)
    private String password;

    @NotNull
    @Size(min = 3, max = 128)
    private String nickname;
}
