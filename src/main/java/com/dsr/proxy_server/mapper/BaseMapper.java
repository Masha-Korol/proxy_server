package com.dsr.proxy_server.mapper;

import java.util.List;

/**
 * This is base mapper class
 * @param <E> representation of entity
 * @param <D> representation of dto
 */
public abstract class BaseMapper<E, D> {

    public abstract E toEntity(D dto);

    public abstract List<E> toEntity(Iterable<D> dtos);

    public abstract D toDto(E entity);

    public abstract List<D> toDto(Iterable<E> entities);
}
