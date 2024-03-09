package com.example.api.service.mapper;

import com.example.api.common.mapper.GenericResponseMapper;
import com.example.api.domain.Post;
import com.example.api.service.dto.PostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostResponseMapper extends GenericResponseMapper<PostResponseDto, Post> {
}
