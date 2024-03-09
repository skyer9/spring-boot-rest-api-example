package com.example.api.domain;

import org.springframework.data.domain.Page;

public interface PostRepositoryCustom {
    Page<Post> search(Integer pageNo, Integer pageSize, String searchStr);
}
