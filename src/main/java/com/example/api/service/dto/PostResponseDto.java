package com.example.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    @Schema(description = "index", nullable = false)
    private Long idx;

    @Schema(description = "title", nullable = false, example = "title")
    private String title;

    @Schema(description = "content", nullable = false, example = "content")
    private String content;

    @Schema(description = "writer", nullable = false, example = "writer")
    private String writer;

    @Schema(description = "view count", nullable = true)
    private Integer viewCnt;

    @Schema(description = "delete YN", nullable = true)
    private Integer deleteYn;

    @Schema(description = "created date", nullable = true)
    private LocalDateTime createdDate;

    @Schema(description = "modified date", nullable = true)
    private LocalDateTime modifiedDate;
}
