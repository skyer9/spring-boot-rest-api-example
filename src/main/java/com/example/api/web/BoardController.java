package com.example.api.web;

import com.example.api.common.ResponseDto;
import com.example.api.service.PostService;
import com.example.api.service.dto.PostInsertRequestDto;
import com.example.api.service.dto.PostUpdateRequestDto;
import com.example.api.service.dto.SearchRequestDto;
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
    public ResponseEntity<?> insert(@RequestBody PostInsertRequestDto dto) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.insert(dto)));
    }

    @PostMapping("/api/v1/upd")
    public ResponseEntity<?> update(@RequestBody PostUpdateRequestDto dto) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.update(dto)));
    }

    @GetMapping("/api/v1/del")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, ""));
    }

    @GetMapping("/api/v1/undel")
    public ResponseEntity<?> undelete(@RequestParam("id") Long id) {
        service.undelete(id);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, ""));
    }

    @GetMapping("/api/v1/")
    public ResponseEntity<?> select(@RequestParam("id") Long id) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.select(id)));
    }

    @GetMapping("/api/v1/search")
    public ResponseEntity<?> search(SearchRequestDto dto) {
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "", service.search(dto)));
    }
}
