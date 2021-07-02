package com.dsr.proxy_server.mapper;

import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationDto;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.ProxyProtocol;
import com.dsr.proxy_server.repositories.CountryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", imports = ProxyProtocol.class)
public abstract class ProxyServerCreationDtoMapper extends BaseMapper<ProxyServer, ProxyServerCreationDto> {

    @Autowired
    protected CountryRepository countryRepository;

    @Mappings({
            @Mapping(target = "country",
                    expression = "java(countryRepository.findByNameEn(dto.getCountryName()))")
    })
    public abstract ProxyServer toEntity(ProxyServerCreationDto dto);
}
