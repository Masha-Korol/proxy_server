package com.dsr.proxy_server.service;

import com.dsr.proxy_server.config.ServersCheckThreadManager;
import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyResultItem;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyServerResponseEntity;
import com.dsr.proxy_server.data.enums.YesNoAny;
import com.dsr.proxy_server.mapper.ProxyResultItemMapper;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import com.dsr.proxy_server.thread.ServersCheckThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProxyServersManagerService {

    private final String PROXY_SERVERS_DATA_SOURCE_URL = "http://api.foxtools.ru/v2/Proxy";
    private final RestTemplate restTemplate;
    private final CountryService countryService;
    private final ProxyServerRepository proxyServerRepository;
    private final ProxyResultItemMapper proxyResultItemMapper;
    private static final Logger logger = LogManager.getLogger(ProxyServersManagerService.class);

    public ProxyServersManagerService(RestTemplate restTemplate,
                                      CountryService countryService, ProxyServerRepository proxyServerRepository,
                                      ProxyResultItemMapper proxyResultItemMapper) {
        this.restTemplate = restTemplate;
        this.countryService = countryService;
        this.proxyServerRepository = proxyServerRepository;
        this.proxyResultItemMapper = proxyResultItemMapper;
    }

    /**
     * This method gets all the proxy servers and analyzes them:
     * if the server is available and it is not in the database, then add it;
     * if the server is not available and it is in the database, then delete it
     */
    @Transactional
    public void checkAllServers() {
        ProxyServerResponseEntity proxyServerResponseEntity = getAllServers();
        List<ProxyResultItem> allServers = proxyServerResponseEntity.getResponse().getItems();
        for (ProxyResultItem proxyResultItem : allServers) {

            // if the server is not in the database yet and it's available
            if (!proxyServerRepository.existsByIp(proxyResultItem.getIp()) &&
                    YesNoAny.Yes.equals(proxyResultItem.getAvailable())) {

                // if country of the current proxy server doesn't exist in the database, then add it
                countryService.add(proxyResultItem.getCountry());
                add(proxyResultItem);
            }
            // if the server is in the database and it's not available, then delete it
            else if (proxyServerRepository.existsByIp(proxyResultItem.getIp()) &&
                    YesNoAny.No.equals(proxyResultItem.getAvailable())) {
                delete(proxyResultItem);
            }
        }
    }

    private void add(ProxyResultItem proxyResultItem) {
        proxyServerRepository.save(proxyResultItemMapper.toEntity(proxyResultItem));
        logger.info("server " + proxyResultItem.getIp() + " has been added");
    }

    private void delete(ProxyResultItem proxyResultItem) {
        proxyServerRepository.delete(proxyResultItemMapper.toEntity(proxyResultItem));
        logger.info("server " + proxyResultItem.getIp() + " has been deleted");
    }

    /**
     * This method gets info about all proxy servers from http://api.foxtools.ru/v2/Proxy
     *
     * @return - entity of type ProxyServerResponseEntity
     */
    private ProxyServerResponseEntity getAllServers() {
        ProxyServerResponseEntity proxyServerResponseEntity =
                restTemplate.getForObject(PROXY_SERVERS_DATA_SOURCE_URL, ProxyServerResponseEntity.class);
        return proxyServerResponseEntity;
    }

    /**
     * This method changes servers' check time interval
     *
     * @param request - entity of type ChangeServersCheckTimingRequest
     */
    public void changeServersCheckTiming(ChangeServersCheckTimingRequest request) {
        Integer intervalInMillis = null;

        switch (request.getTimeUnit()) {
            case DAYS:
                intervalInMillis = request.getInterval() * 3600000 * 24;
                break;
            case HOURS:
                intervalInMillis = request.getInterval() * 3600000;
                break;
            case MINUTES:
                intervalInMillis = request.getInterval() * 60000;
                break;
            case SECONDS:
                intervalInMillis = request.getInterval() * 1000;
                break;
        }

        // change the time interval and notify the thread, that time interval has changed
        if (intervalInMillis != null) {
            ServersCheckThread.checkServersTimeInterval = intervalInMillis;
            synchronized (ServersCheckThreadManager.serversCheckThread) {
                ServersCheckThreadManager.serversCheckThread.notify();
            }
        }
    }
}
