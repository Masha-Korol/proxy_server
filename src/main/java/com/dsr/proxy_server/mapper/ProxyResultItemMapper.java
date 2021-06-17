package com.dsr.proxy_server.mapper;

import com.dsr.proxy_server.data.dto.ProxyResultItem;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.repositories.CountryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProxyResultItemMapper extends BaseMapper<ProxyServer, ProxyResultItem> {

    @Autowired
    protected CountryRepository countryRepository;

    @Mapping(target = "country",
            expression = "java(countryRepository.findByNameEn(dto.getCountry().getNameEn()))")
    public abstract ProxyServer toEntity(ProxyResultItem dto);
}
