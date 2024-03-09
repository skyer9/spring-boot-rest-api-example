package com.example.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostInsertRequestDto {
    @Schema(description = "title", nullable = false, example = "title")
    private String title;

    @Schema(description = "content", nullable = false, example = "content")
    private String content;

    @Schema(description = "writer", nullable = false, example = "writer")
    private String writer;
}
