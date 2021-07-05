package com.dsr.proxy_server.service;

import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationDto;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.CountryResult;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyResultItem;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyServerResponseEntity;
import com.dsr.proxy_server.data.dto.pagination.PageDto;
import com.dsr.proxy_server.data.dto.pagination.PageRequestDto;
import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationResponseDto;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.ProxySourceType;
import com.dsr.proxy_server.data.enums.YesNoAny;
import com.dsr.proxy_server.mapper.ProxyResultItemMapper;
import com.dsr.proxy_server.mapper.ProxyServerCreationDtoMapper;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;

/**
 * This class contains the logic that's connected with proxy servers add/get operations.
 */
@Service
public class ProxyServersManagerService {

    private final String PROXY_SERVERS_DATA_SOURCE_URL = "http://api.foxtools.ru/v2/Proxy";
    private final RestTemplate restTemplate;
    private final CountryService countryService;
    private final ProxyMaintenanceService proxyMaintenanceService;
    private final ProxyServerRepository proxyServerRepository;
    private final ProxyResultItemMapper proxyResultItemMapper;
    private final ProxyServerCreationDtoMapper proxyServerCreationDtoMapper;
    private static final Logger logger = LogManager.getLogger(ProxyServersManagerService.class);

    public ProxyServersManagerService(RestTemplate restTemplate,
                                      CountryService countryService, ProxyMaintenanceService proxyMaintenanceService, ProxyServerRepository proxyServerRepository,
                                      ProxyResultItemMapper proxyResultItemMapper, ProxyServerCreationDtoMapper proxyServerCreationDtoMapper) {
        this.restTemplate = restTemplate;
        this.countryService = countryService;
        this.proxyMaintenanceService = proxyMaintenanceService;
        this.proxyServerRepository = proxyServerRepository;
        this.proxyResultItemMapper = proxyResultItemMapper;
        this.proxyServerCreationDtoMapper = proxyServerCreationDtoMapper;
    }

    /**
     * This method gets all the proxy servers and analyzes them:
     * if the server is not in the database, add it
     * if the availability of the server in the db is incorrect, update it
     */
    @Transactional
    public void checkAllServers() {
        ProxyServerResponseEntity proxyServerResponseEntity = getAllServers();
        List<ProxyResultItem> allServers = proxyServerResponseEntity.getResponse().getItems();
        for (ProxyResultItem proxyResultItem : allServers) {

            // if the server is not in the database yet
            if (!proxyServerRepository.existsByIp(proxyResultItem.getIp())) {
                ProxyServer proxyServer = proxyResultItemMapper.toEntity(proxyResultItem);
                proxyServer.setSource(ProxySourceType.AUTO);
                CountryResult countryResult = proxyResultItem.getCountry();
                // TODO раскомментить
                add(proxyServer, countryResult);
            }
        }
    }

    /**
     * This method adds proxy to database (if it's available)
     *
     * @param proxyServer entity of type ProxyServer
     */
    private boolean add(ProxyServer proxyServer, CountryResult countryResult) {
        if (proxyMaintenanceService.ifProxyAvailable(proxyServer)) {
            // if country of the current proxy server doesn't exist in the database, then add it
            Country country = countryService.add(countryResult);
            proxyMaintenanceService.updateLastTimeCheckDate(proxyServer);
            proxyServer.setCountry(country);
            proxyServer.setWorkload(0);
            proxyServer.setAvailable(YesNoAny.Yes);
            proxyServerRepository.save(proxyServer);
            logger.info("server " + proxyServer.toString() + " has been added");
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method adds proxy server to database (getting data about proxy server from post request)
     *
     * @param proxyServerCreationDto entity of type ProxyServerCreationDto
     */
    @Transactional
    public ProxyServerCreationResponseDto addProxyServerByRequest(ProxyServerCreationDto proxyServerCreationDto) {
        ProxyServer proxyServer = proxyServerCreationDtoMapper.toEntity(proxyServerCreationDto);
        proxyServer.setSource(ProxySourceType.MANUAL);
        CountryResult countryResult = new CountryResult(proxyServerCreationDto.getCountryName());
        if (!proxyServerRepository.existsByIp(proxyServer.getIp())) {
            boolean added = add(proxyServer, countryResult);
            if (added) {
                return new ProxyServerCreationResponseDto("Proxy server has been successfully added: " +
                        proxyServerCreationDto.toString());
            } else {
                return new ProxyServerCreationResponseDto("Proxy server is unavailable");
            }
        } else {
            return new ProxyServerCreationResponseDto("Proxy with given ip already exixts in the database");
        }
    }

    /**
     * This method deleted proxy from database
     *
     * @param proxyResultItem entity of type ProxyResultItem
     */
    private void delete(ProxyResultItem proxyResultItem) {
        ProxyServer proxyServer = proxyResultItemMapper.toEntity(proxyResultItem);
        proxyServerRepository.delete(proxyServer);
        logger.info("server " + proxyServer.toString() + " has been deleted");
    }

    /**
     * This method gets info about all proxy servers from http://api.foxtools.ru/v2/Proxy
     *
     * @return entity of type ProxyServerResponseEntity
     */
    private ProxyServerResponseEntity getAllServers() {
        ProxyServerResponseEntity proxyServerResponseEntity =
                restTemplate.getForObject(PROXY_SERVERS_DATA_SOURCE_URL, ProxyServerResponseEntity.class);
        return proxyServerResponseEntity;
    }

    /**
     * This method returns all the available proxy servers in the form of page
     *
     * @param pageRequest entity of type PageRequestDto
     * @return list of entities of type PageDto
     */
    public PageDto<ProxyServer> getAll(PageRequestDto pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getItemsPerPage(),
                pageRequest.getSort());
        Page<ProxyServer> page = proxyServerRepository.findAll(pageable);
        // filter only available ones
        page.filter(proxyServer -> YesNoAny.Yes.equals(proxyServer.getAvailable()));
        return new PageDto<ProxyServer>(page);
    }
}
