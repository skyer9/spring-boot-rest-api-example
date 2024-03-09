package com.example.api.service.mapper;

import com.example.api.domain.Post;
import com.example.api.common.mapper.GenericRequestMapper;
import com.example.api.service.dto.PostInsertRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostInsertRequestMapper extends GenericRequestMapper<PostInsertRequestDto, Post> {
}
