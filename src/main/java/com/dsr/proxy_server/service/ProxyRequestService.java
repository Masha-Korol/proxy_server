package com.dsr.proxy_server.service;

import com.dsr.proxy_server.data.dto.proxy.ProxyServerRequest;
import com.dsr.proxy_server.data.dto.proxy.ProxyServerResponse;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.ContentType;
import com.dsr.proxy_server.data.enums.Method;
import com.dsr.proxy_server.repositories.CountryRepository;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProxyRequestService {

    private final ProxyServerRepository proxyServerRepository;
    private final CountryRepository countryRepository;

    public ProxyRequestService(ProxyServerRepository proxyServerRepository, CountryRepository countryRepository) {
        this.proxyServerRepository = proxyServerRepository;
        this.countryRepository = countryRepository;
    }

    /**
     * This method directs coming requests to proxy servers based on preferable country.
     * Request for all the servers is made with the help of spring data jpa sorting.
     * Sorting is executed by fields:
     * anonymity (the higher the better);
     * uptime (the less the better).
     * Also workload is taken into consideration (the less the better)
     *
     * @param request request entity of type ProxyServerRequest
     * @return response entity of type ProxyServerResponse
     */
    @Transactional
    public ProxyServerResponse directRequestToProxyServer(ProxyServerRequest request) {
        // request validation
        ProxyServerResponse validationResult = validateProxyRequest(request);
        if (validationResult != null) {
            return validationResult;
        }

        Country country = countryRepository.findByNameEn(request.getCountry());
        List<ProxyServer> proxyServers = proxyServerRepository.findAllByCountry(country, Sort.by("uptime"));
        // request's country validation
        if (proxyServers.size() == 0) {
            return new ProxyServerResponse(null, 400,
                    "Bad Request. There's no available proxy servers in " + request.getCountry());
        }
        // if success
        return new ProxyServerResponse(proxyServers.get(0).getIp(), 200,
                "OK. Your request has been successfully sent to proxy with ip " + proxyServers.get(0).getIp());
    }

    /**
     * This method validates request.
     * It checks content type accordance and presence of data in case of POST and PUT methods
     *
     * @param request request to be validated of type ProxyServerRequest
     * @return entity of type ProxyServerResponse
     */
    private ProxyServerResponse validateProxyRequest(ProxyServerRequest request) {
        if ((Method.POST.equals(request.getMethod()) ||
                Method.PUT.equals(request.getMethod())) && (request.getPayload() == null)) {
            return new ProxyServerResponse(null, 400,
                    "Bad Request. POST and PUT methods must have data");
        }
        if ((ContentType.JSON.equals(request.getContentType()) &&
                request.getPayload().getClass().getName().equals("java.lang.String")) ||
                (ContentType.TEXT.equals(request.getContentType()) &&
                        !request.getPayload().getClass().getName().equals("java.lang.String"))) {
            return new ProxyServerResponse(null, 400,
                    "Bad Request. Content type doesn't correspond to payload type");
        }
        return null;
    }

}
