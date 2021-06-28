package com.dsr.proxy_server.service;

import com.dsr.proxy_server.config.ThreadManager;
import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyResultItem;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyServerResponseEntity;
import com.dsr.proxy_server.data.dto.pagination.PageDto;
import com.dsr.proxy_server.data.dto.pagination.PageRequestDto;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.YesNoAny;
import com.dsr.proxy_server.mapper.ProxyResultItemMapper;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import com.dsr.proxy_server.thread.ServersCheckThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the logic that's connected with proxy servers updating:
 * adding them to db;
 * work with threads;
 * updating their workload.
 */
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
                // if country of the current proxy server doesn't exist in the database, then add it
                countryService.add(proxyResultItem.getCountry());
                add(proxyResultItem);
            } else {
                // if the availability of the server in the db is different from the actual availability,
                // update the one in the db
                if (!proxyServerRepository.findByIp(proxyResultItem.getIp()).getAvailable().equals(
                        proxyResultItem.getAvailable())) {
                    changeProxyServerAvailability(proxyResultItem);
                } else {
                    ProxyServer proxyServer = proxyServerRepository.findByIp(proxyResultItem.getIp());
                    updateLastTimeCheckDate(proxyServer);
                    proxyServerRepository.save(proxyServer);
                }
            }
        }
    }

    private void add(ProxyResultItem proxyResultItem) {
        ProxyServer proxyServer = proxyResultItemMapper.toEntity(proxyResultItem);
        updateLastTimeCheckDate(proxyServer);
        proxyServer.setWorkload(0);
        proxyServerRepository.save(proxyServer);
        logger.info("server " + proxyServer.toString() + " has been added");
    }

    private void delete(ProxyResultItem proxyResultItem) {
        ProxyServer proxyServer = proxyResultItemMapper.toEntity(proxyResultItem);
        proxyServerRepository.delete(proxyServer);
        logger.info("server " + proxyServer.toString() + " has been deleted");
    }

    /**
     * This method updates the time of the last check for availability (sets current date and time)
     *
     * @param proxyServer the proxy sever, whose last time check date needs to be updated
     */
    private void updateLastTimeCheckDate(ProxyServer proxyServer) {
        proxyServer.setLastTimeCheck(new Date());
    }

    /**
     * This method changes the availability of the proxy server
     *
     * @param proxyResultItem - the entity of type ProxyResultItem, that contains actual info about the proxy server
     */
    private void changeProxyServerAvailability(ProxyResultItem proxyResultItem) {
        ProxyServer proxyServer = proxyServerRepository.findByIp(proxyResultItem.getIp());
        proxyServer.setAvailable(proxyResultItem.getAvailable());
        updateLastTimeCheckDate(proxyServer);
        proxyServerRepository.save(proxyServer);
        logger.info("server " + proxyResultItem.getIp() + " availability has been changed to " +
                proxyResultItem.getAvailable() + ". Proxy: " + proxyServer.toString());
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
     * This method changes servers' check time interval
     *
     * @param request entity of type ChangeServersCheckTimingRequest
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
            synchronized (ThreadManager.serversCheckThread) {
                ThreadManager.serversCheckThread.notify();
            }
        }
    }

    /**
     * This method nullifies workload of every proxy server in the db.
     * It is called by a ServersWorkloadControlThread once in a certain time interval
     */
    @Transactional
    public void nullifyWorkload() {
        Iterable<ProxyServer> allProxyServers = proxyServerRepository.findAll();
        for (ProxyServer proxyServer : allProxyServers) {
            proxyServer.setWorkload(0);
            proxyServerRepository.save(proxyServer);
        }
    }

    public PageDto<ProxyServer> getAll(PageRequestDto pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getItemsPerPage(),
                pageRequest.getSort());
        Page<ProxyServer> page = proxyServerRepository.findAll(pageable);
        // filter only available ones
        page.filter(proxyServer -> YesNoAny.Yes.equals(proxyServer.getAvailable()));
        return new PageDto<ProxyServer>(page);
        /*return allProxyServers.stream().filter(proxyServer -> YesNoAny.Yes.equals(proxyServer.getAvailable())).
                collect(Collectors.toList());*/
    }
}
