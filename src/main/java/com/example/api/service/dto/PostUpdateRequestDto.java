package com.example.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDto {
    @Schema(description = "index")
    private Long idx;

    @Schema(description = "title", example = "title")
    private String title;

    @Schema(description = "content", example = "content")
    private String content;

    @Schema(description = "writer", example = "writer")
    private String writer;
}
