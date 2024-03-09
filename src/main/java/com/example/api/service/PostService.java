package com.example.api.service;

import com.example.api.common.DeleteStatus;
import com.example.api.domain.Post;
import com.example.api.domain.PostRepository;
import com.example.api.service.dto.PostInsertRequestDto;
import com.example.api.service.dto.PostResponseDto;
import com.example.api.service.dto.PostUpdateRequestDto;
import com.example.api.service.dto.SearchRequestDto;
import com.example.api.service.mapper.PostInsertRequestMapper;
import com.example.api.service.mapper.PostResponseMapper;
import com.example.api.service.mapper.PostUpdateRequestMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostInsertRequestMapper insertMapper = Mappers.getMapper(PostInsertRequestMapper.class);
    private final PostUpdateRequestMapper updateMapper = Mappers.getMapper(PostUpdateRequestMapper.class);
    private final PostResponseMapper responseMapper = Mappers.getMapper(PostResponseMapper.class);

    @Transactional
    public PostResponseDto insert(PostInsertRequestDto dto) {
        Post e = new Post();
        insertMapper.updateFromDto(dto, e);
        return responseMapper.toDto(postRepository.save(e));
    }

    @Transactional
    public PostResponseDto update(PostUpdateRequestDto dto) {
        Post e = get(dto.getIdx());
        updateMapper.updateFromDto(dto, e);
        e.setModifiedDate(LocalDateTime.now());
        return responseMapper.toDto(e);
    }

    @Transactional
    public void delete(Long idx) {
        Post e = get(idx);
        e.setModifiedDate(LocalDateTime.now());
        e.setDeleteYn(DeleteStatus.DELETE.getValue());
    }

    @Transactional
    public void undelete(Long idx) {
        Post e = get(idx);
        e.setModifiedDate(LocalDateTime.now());
        e.setDeleteYn(DeleteStatus.UNDELETE.getValue());
    }

    @Transactional
    public PostResponseDto select(Long idx) {
        Post e = get(idx);
        e.setViewCnt(e.getViewCnt() + 1);
        return responseMapper.toDto(e);
    }

    public List<PostResponseDto> search(SearchRequestDto dto) {
        if (dto.getPg() == null || dto.getPg() < 0) {
            dto.setPg(0);
        }
        if (dto.getSz() == null || dto.getSz() < 1 || dto.getSz() > 50) {
            dto.setSz(50);
        }
        Page<Post> list = postRepository.search(dto.getPg(), dto.getSz(), dto.getS());
        return responseMapper.toDto(list.getContent());
    }

    private Post get(Long idx) {
        Optional<Post> e = postRepository.findById(idx);
        if (e.isEmpty()) {
            throw new RuntimeException("data not found");
        }
        return e.get();
    }
}
