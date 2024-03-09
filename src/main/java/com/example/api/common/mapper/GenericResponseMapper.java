package com.example.api.common.mapper;

import java.util.List;

public interface GenericResponseMapper<D, E> {
    D toDto(E e);
    List<D> toDto(List<E> e);
}
