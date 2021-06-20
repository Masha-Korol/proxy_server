package com.dsr.proxy_server.mapper;

import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyResultItem;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.ProxyProtocol;
import com.dsr.proxy_server.repositories.CountryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is mapper that converts the entity of ProxyServer to the entity of ProxyResultItem and back
 */
@Mapper(componentModel = "spring", imports = ProxyProtocol.class)
public abstract class ProxyResultItemMapper extends BaseMapper<ProxyServer, ProxyResultItem> {

    @Autowired
    protected CountryRepository countryRepository;

    @Mappings({
            @Mapping(target = "country",
                    expression = "java(countryRepository.findByNameEn(dto.getCountry().getNameEn()))"),
            @Mapping(target = "type",
                    expression = "java(mapTypeVariableToEnum(dto.getType()))")
    })
    public abstract ProxyServer toEntity(ProxyResultItem dto);

    @Mapping(target = "type",
            expression = "java(mapTypeVariableToInteger(entity.getType()))")
    public abstract ProxyResultItem toDto(ProxyServer entity);

    /**
     * This class is used to map the entity of type Integer to the entity of type ProxyProtocol
     * for converting the entity of ProxyResultItem to the entity of ProxyServer
     * @param value value of Integer
     * @return value of ProxyProtocol
     */
    protected ProxyProtocol mapTypeVariableToEnum(Integer value) {
        switch (value) {
            case 0:
                return ProxyProtocol.None;
            case 1:
                return ProxyProtocol.HTTP;
            case 2:
                return ProxyProtocol.HTTPS;
            case 4:
                return ProxyProtocol.SOCKS4;
            case 8:
                return ProxyProtocol.SOCKS5;
            case 15:
                return ProxyProtocol.ALL;
        }
        return null;
    }

    /**
     * This class is used to map the entity of type ProxyProtocol to the entity of type Integer
     * for converting the entity of ProxyServer to the entity of ProxyResultItem
     * @param enumValue value of ProxyProtocol
     * @return value of Integer
     */
    protected Integer mapTypeVariableToInteger(ProxyProtocol enumValue) {
        switch (enumValue) {
            case None:
                return 0;
            case HTTP:
                return 1;
            case HTTPS:
                return 2;
            case SOCKS4:
                return 4;
            case SOCKS5:
                return 8;
            case ALL:
                return 15;
        }
        return -1;
    }
}
