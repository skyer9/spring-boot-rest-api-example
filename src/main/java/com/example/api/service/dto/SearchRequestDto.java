package com.example.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRequestDto {
    @Schema(description = "page number", nullable = true, example = "0")
    private Integer pg;

    @Schema(description = "page size", nullable = true, example = "20")
    private Integer sz;

    @Schema(description = "search string", nullable = true, example = "")
    private String s;
}
