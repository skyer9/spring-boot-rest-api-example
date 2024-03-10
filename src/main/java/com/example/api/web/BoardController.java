package com.example.api.web;

import com.example.api.common.ResponseDto;
import com.example.api.service.PostService;
import com.example.api.service.dto.PostInsertRequestDto;
import com.example.api.service.dto.PostUpdateRequestDto;
import com.example.api.service.dto.SearchRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Board", description = "Board API")
public class BoardController {
    private final PostService service;

    @PostMapping("/api/v1/ins")
    @Operation(summary = "insert post", description = "insert post")
    public ResponseEntity<?> insert(@RequestBody PostInsertRequestDto dto) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.insert(dto)));
    }

    @PostMapping("/api/v1/upd")
    @Operation(summary = "update post", description = "update post")
    public ResponseEntity<?> update(@RequestBody PostUpdateRequestDto dto) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.update(dto)));
    }

    @GetMapping("/api/v1/del")
    @Operation(summary = "delete post", description = "delete post")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, ""));
    }

    @GetMapping("/api/v1/undel")
    @Operation(summary = "undelete post", description = "undelete post")
    public ResponseEntity<?> undelete(@RequestParam("id") Long id) {
        service.undelete(id);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, ""));
    }

    @GetMapping("/api/v1/")
    @Operation(summary = "select post", description = "select post")
    public ResponseEntity<?> select(@RequestParam("id") Long id) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.select(id)));
    }

    @GetMapping("/api/v1/search")
    @Operation(summary = "search post", description = "search post")
    public ResponseEntity<?> search(SearchRequestDto dto) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.search(dto)));
    }
}
