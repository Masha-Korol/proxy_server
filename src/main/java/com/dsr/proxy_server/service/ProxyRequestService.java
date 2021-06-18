package com.dsr.proxy_server.service;

import com.dsr.proxy_server.data.dto.proxy.ProxyServerRequest;
import com.dsr.proxy_server.data.dto.proxy.ProxyServerResponse;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.repositories.CountryRepository;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class ProxyRequestService {

    private final ProxyServerRepository proxyServerRepository;
    private final CountryRepository countryRepository;

    public ProxyRequestService(ProxyServerRepository proxyServerRepository, CountryRepository countryRepository) {
        this.proxyServerRepository = proxyServerRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional
    public ProxyServerResponse directRequestToProxyServer(ProxyServerRequest request){
        Country country = countryRepository.findByNameEn(request.getCountry());
        List<ProxyServer> proxyServers = proxyServerRepository.findAllByCountry(country);
        ProxyServerResponse proxyServerResponse = new ProxyServerResponse();
        proxyServerResponse.setIp(proxyServers.get(0).getIp());
        proxyServerResponse.setStatusCode(500);
        proxyServerResponse.setResponseBody("все ок");
        return proxyServerResponse;
    }
}
