package com.example.api.common.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface GenericRequestMapper<D, E> {
    void updateFromDto(D dto, @MappingTarget E entity);
}
