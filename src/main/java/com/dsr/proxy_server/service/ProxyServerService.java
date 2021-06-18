package com.dsr.proxy_server.service;

import com.dsr.proxy_server.config.ServersCheckThreadManager;
import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyResultItem;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyServerResponseEntity;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.enums.YesNoAny;
import com.dsr.proxy_server.mapper.ProxyResultItemMapper;
import com.dsr.proxy_server.repositories.CountryRepository;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import com.dsr.proxy_server.thread.ServersCheckThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProxyServerService {

    private final RestTemplate restTemplate;
    private final ProxyServerRepository proxyServerRepository;
    private final CountryRepository countryRepository;
    private final ProxyResultItemMapper proxyResultItemMapper;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ServersCheckThread serversCheckThread;

    public ProxyServerService(RestTemplate restTemplate,
                              ProxyServerRepository proxyServerRepository,
                              CountryRepository countryRepository,
                              ProxyResultItemMapper proxyResultItemMapper) {
        this.restTemplate = restTemplate;
        this.proxyServerRepository = proxyServerRepository;
        this.countryRepository = countryRepository;
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
                if (!countryRepository.existsByNameEn(proxyResultItem.getCountry().getNameEn())) {
                    Country country = new Country();
                    country.setNameEn(proxyResultItem.getCountry().getNameEn());
                    countryRepository.save(country);
                    System.out.println("country " + country.getNameEn() + " has been added");
                }

                proxyServerRepository.save(proxyResultItemMapper.toEntity(proxyResultItem));
                System.out.println("server " + proxyResultItem.getIp() + " has been added");
            }
            // if the server is in the database and it's not available
            else if (proxyServerRepository.existsByIp(proxyResultItem.getIp()) &&
                    YesNoAny.No.equals(proxyResultItem.getAvailable())) {
                proxyServerRepository.delete(proxyResultItemMapper.toEntity(proxyResultItem));
            }
        }
    }

    private ProxyServerResponseEntity getAllServers() {
        String url = "http://api.foxtools.ru/v2/Proxy";
        ProxyServerResponseEntity proxyServerResponseEntity = restTemplate.getForObject(url, ProxyServerResponseEntity.class);
        return proxyServerResponseEntity;
    }

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

        if (intervalInMillis != null) {
            ServersCheckThread.checkServersTimeInterval = intervalInMillis;
            synchronized (ServersCheckThreadManager.serversCheckThread){
                ServersCheckThreadManager.serversCheckThread.notify();
            }
        }
    }
}
