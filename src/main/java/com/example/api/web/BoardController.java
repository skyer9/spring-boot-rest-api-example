package com.example.api.web;

import com.example.api.service.PostService;
import com.example.api.service.dto.PostInsertRequestDto;
import com.example.api.service.dto.PostResponseDto;
import com.example.api.service.dto.PostUpdateRequestDto;
import com.example.api.service.dto.SearchRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "001. Board", description = "Board API")
public class BoardController {
    private final PostService service;

    @PostMapping("/api/v1/ins")
    @Operation(summary = "insert post", description = "insert post")
    public PostResponseDto insert(@RequestBody PostInsertRequestDto dto) {
        return service.insert(dto);
    }

    @PostMapping("/api/v1/upd")
    @Operation(summary = "update post", description = "update post")
    public PostResponseDto update(@RequestBody PostUpdateRequestDto dto) {
        return service.update(dto);
    }

    @GetMapping("/api/v1/del")
    public void delete(@RequestParam("id") Long id) {
        service.delete(id);
    }

    @GetMapping("/api/v1/undel")
    public void undelete(@RequestParam("id") Long id) {
        service.undelete(id);
    }

    @GetMapping("/api/v1/")
    public PostResponseDto select(@RequestParam("id") Long id) {
        return service.select(id);
    }

    @GetMapping("/api/v1/search")
    public List<PostResponseDto> search(SearchRequestDto dto) {
        return service.search(dto);
    }
}
