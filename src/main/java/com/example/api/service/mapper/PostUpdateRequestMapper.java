package com.example.api.service.mapper;

import com.example.api.domain.Post;
import com.example.api.common.mapper.GenericRequestMapper;
import com.example.api.service.dto.PostUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostUpdateRequestMapper extends GenericRequestMapper<PostUpdateRequestDto, Post> {
}
