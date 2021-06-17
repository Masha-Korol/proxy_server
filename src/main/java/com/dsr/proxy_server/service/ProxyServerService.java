package com.dsr.proxy_server.service;

import com.dsr.proxy_server.data.dto.ProxyServerResponseEntity;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.YesNoAny;
import com.dsr.proxy_server.mapper.ProxyResultItemMapper;
import com.dsr.proxy_server.repositories.CountryRepository;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import com.dsr.proxy_server.thread.ServersCheckThread;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProxyServerService {

    private Integer checkServersTimeInterval = 10000;
    private final RestTemplate restTemplate;
    private final ProxyServerRepository proxyServerRepository;
    private final CountryRepository countryRepository;
    private final ProxyResultItemMapper proxyResultItemMapper;

    public ProxyServerService(RestTemplate restTemplate,
                              ProxyServerRepository proxyServerRepository,
                              CountryRepository countryRepository,
                              ProxyResultItemMapper proxyResultItemMapper) {
        this.restTemplate = restTemplate;
        this.proxyServerRepository = proxyServerRepository;
        this.countryRepository = countryRepository;
        this.proxyResultItemMapper = proxyResultItemMapper;
    }

    public void startServersCheckThread() {
        ServersCheckThread serversCheckThread = new ServersCheckThread(checkServersTimeInterval, this);
        Thread thread = new Thread(serversCheckThread);
        thread.start();
        System.out.println("thread has just started");
    }

    /**
     * This method gets all the proxy servers and analyzes them:
     * if the server is available and it is not in the database, then add it;
     * if the server is not available and it is in the database, then delete it
     */
    public void checkAllServers() {
        List<ProxyServer> allServers = getAllServers();
        for (ProxyServer proxyServer : allServers) {
            // if the server is not in the database yet and it's available
            if (!proxyServerRepository.existsByIp(proxyServer.getIp()) &&
                    YesNoAny.Yes.equals(proxyServer.getAvailable())) {

                // if country of current server doesn't exist in the database, then add it
                if (proxyServer.getCountry() == null){
                    Country country = new Country();
                    // TODO do something if there's no such country
                    countryRepository.save();
                }

                proxyServerRepository.save(proxyServer);
            }
            // if the server is in the database and it's not available
            else if (proxyServerRepository.existsByIp(proxyServer.getIp()) &&
                    YesNoAny.No.equals(proxyServer.getAvailable())) {
                proxyServerRepository.delete(proxyServer);
            }
        }
    }

    private List<ProxyServer> getAllServers() {
        String url = "http://api.foxtools.ru/v2/Proxy";
        ProxyServerResponseEntity forObject = restTemplate.getForObject(url, ProxyServerResponseEntity.class);
        if (forObject != null) {
            return proxyResultItemMapper.toEntity(forObject.getResponse().getItems());
        }
        return null;
    }
}
