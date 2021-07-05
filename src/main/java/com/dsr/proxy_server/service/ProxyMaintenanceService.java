package com.dsr.proxy_server.service;

import com.dsr.proxy_server.config.ThreadManager;
import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.ProxyServersResponse.ProxyResultItem;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.YesNoAny;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import com.dsr.proxy_server.thread.ServersCheckThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * This class contains the logic that's connected with the maintenance of the proxy servers:
 * checking/updating their availability;
 * nullifying workload;
 * changing thread's check timing.
 */
@Service
public class ProxyMaintenanceService {

    private final String PROXY_SERVERS_DATA_SOURCE_URL = "http://api.foxtools.ru/v2/Proxy";
    private final ProxyServerRepository proxyServerRepository;
    private static final Logger logger = LogManager.getLogger(ProxyMaintenanceService.class);

    public ProxyMaintenanceService(ProxyServerRepository proxyServerRepository) {
        this.proxyServerRepository = proxyServerRepository;
    }

    /**
     * This method checks if the passed proxy server is available
     *
     * @param proxyServer entity of type ProxyServer - server to be checked
     * @return true if proxy is available, false otherwise
     */
    public boolean ifProxyAvailable(ProxyServer proxyServer) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress(proxyServer.getIp(), proxyServer.getPort())))
                .build();
        HttpRequest newHttpRequest = null;
        try {
            newHttpRequest = HttpRequest
                    .newBuilder(new URI(PROXY_SERVERS_DATA_SOURCE_URL))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(newHttpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                logger.warn("server with ip " + proxyServer.getIp() + " is unavailable");
                return false;
            }
            return true;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.warn("server with ip " + proxyServer.getIp() + " is unavailable");
            return false;
        }
    }

    /**
     * This method updates the time of the last check for availability (sets current date and time)
     *
     * @param proxyServer the proxy sever, whose last time check date needs to be updated
     */
    public void updateLastTimeCheckDate(ProxyServer proxyServer) {
        proxyServer.setLastTimeCheck(new Date());
    }

    /**
     * This method changes the availability of the proxy server
     *
     * @param proxyResultItem - the entity of type ProxyResultItem, that contains actual info about the proxy server
     */
    public void changeProxyServerAvailability(ProxyResultItem proxyResultItem) {
        ProxyServer proxyServer = proxyServerRepository.findByIp(proxyResultItem.getIp());
        proxyServer.setAvailable(proxyResultItem.getAvailable());
        updateLastTimeCheckDate(proxyServer);
        proxyServerRepository.save(proxyServer);
        logger.info("server " + proxyResultItem.getIp() + " availability has been changed to " +
                proxyResultItem.getAvailable() + ". Proxy: " + proxyServer.toString());
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

    /**
     * This method updates availability of all the servers
     */
    @Transactional
    public void updateAllProxyServersAvailability() {
        List<ProxyServer> allProxyServers = (List<ProxyServer>) proxyServerRepository.findAll();
        for (ProxyServer proxyServer : allProxyServers) {
            if (ifProxyAvailable(proxyServer)) {
                proxyServer.setAvailable(YesNoAny.Yes);
            } else {
                proxyServer.setAvailable(YesNoAny.No);
            }
            proxyServerRepository.save(proxyServer);
        }
    }
}
