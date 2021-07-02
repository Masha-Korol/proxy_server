package com.dsr.proxy_server.service;

import com.dsr.proxy_server.data.dto.proxy.ProxyServerRequest;
import com.dsr.proxy_server.data.dto.proxy.ProxyServerResponse;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.*;
import com.dsr.proxy_server.repositories.CountryRepository;
import com.dsr.proxy_server.repositories.ProxyServerRepository;
import com.dsr.proxy_server.sorting.MergeSorter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.wagon.ConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the logic that's connected with users' requests
 */
@Service
public class ProxyRequestService {

    private final ProxyServerRepository proxyServerRepository;
    private final CountryRepository countryRepository;
    private final MergeSorter mergeSorter;
    private static final Logger logger = LogManager.getLogger(ProxyRequestService.class);

    public ProxyRequestService(ProxyServerRepository proxyServerRepository, CountryRepository countryRepository, MergeSorter mergeSorter) {
        this.proxyServerRepository = proxyServerRepository;
        this.countryRepository = countryRepository;
        this.mergeSorter = mergeSorter;
    }

    /**
     * This method directs coming requests to proxy servers based on preferable country.
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
        List<ProxyServer> proxyServers = proxyServerRepository.findAllByCountry(country);
        // request's country validation
        if (proxyServers.size() == 0) {
            logger.warn("Bad Request. There's no available proxy servers in the given country. Request: " +
                    request.toString());
            return new ProxyServerResponse(null, 400,
                    "Bad Request. There's no available proxy servers in " + request.getCountry());
        }
        ProxyServer proxyServer = getTheBestProxyServerOfAll(proxyServers, request.getProxyProtocol());
        ProxyServerResponse proxyServerResponse = redirectRequestToProxyServer(request, proxyServer);
        logger.info("The response has been sent : " + proxyServerResponse.toString());
        if (proxyServerResponse.getIp() != null) {
            updateProxyServerWorkload(proxyServer);
        }
        return proxyServerResponse;
    }

    /**
     * This method sends request through the chosen proxy server
     *
     * @param request     request to be sent
     * @param proxyServer proxy through which request needs to be sent
     * @return response of type ProxyServerResponse
     */
    private ProxyServerResponse redirectRequestToProxyServer(ProxyServerRequest request, ProxyServer proxyServer) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress(proxyServer.getIp(), proxyServer.getPort())))
                // .proxy(ProxySelector.of(new InetSocketAddress("185.23.80.17", 8080)))
                .build();
        try {
            HttpRequest newHttpRequest = null;
            Method method = request.getMethod();
            switch (method) {
                case PUT:
                    newHttpRequest = HttpRequest
                            .newBuilder(new URI(request.getUrl()))
                            .header("Content-Type", getContentTypeInCorrectFormat(request.getContentType()))
                            .PUT(HttpRequest.BodyPublishers.ofString(request.getPayload().toString()))
                            .build();
                    break;
                case POST:
                    newHttpRequest = HttpRequest
                            .newBuilder(new URI(request.getUrl()))
                            .header("Content-Type", getContentTypeInCorrectFormat(request.getContentType()))
                            .POST(HttpRequest.BodyPublishers.ofString(request.getPayload().toString()))
                            .build();
                    break;
                case GET:
                    newHttpRequest = HttpRequest
                            .newBuilder(new URI(request.getUrl()))
                            .GET()
                            .build();
                    break;
                case DELETE:
                    newHttpRequest = HttpRequest
                            .newBuilder(new URI(request.getUrl()))
                            .DELETE()
                            .build();
                    break;
            }
            HttpResponse<String> response = client.send(newHttpRequest, HttpResponse.BodyHandlers.ofString());
            return new ProxyServerResponse(proxyServer.getIp(), response.statusCode(), response.body());
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error(e.getMessage());
            return new ProxyServerResponse(null, null,
                    "An error occurred: " + e.getMessage());
        }
    }

    /**
     * This method converts entity of type ContentType to entity of type string for newHttpRequest
     *
     * @param contentType entity of type ContentType that needs to be converted
     * @return converted entity
     */
    private String getContentTypeInCorrectFormat(ContentType contentType) {
        switch (contentType) {
            case JSON:
                return "application/json";
            case TEXT:
                return "text/plain";
        }
        return "";
    }

    /**
     * This method updates workload of the proxy server (increases it by one for every request)
     *
     * @param proxyServer entity of type ProxyServer whose workload needs to be increased
     */
    private void updateProxyServerWorkload(ProxyServer proxyServer) {
        proxyServer.setWorkload(proxyServer.getWorkload() + 1);
        logger.info("The workload of the server has been changed: " + proxyServer.toString());
    }

    /**
     * This method sorts all the proxy servers by anonymity and uptime, then returns the best result
     *
     * @param proxyServers list of all proxy servers
     * @return the most suitable proxy server
     */
    private ProxyServer getTheBestProxyServerOfAll(List<ProxyServer> proxyServers,
                                                   ProxyProtocol preferableType) {
        // choose servers with preferable proxy protocol if there are any
        List<ProxyServer> proxyServersByProtocolList = new ArrayList<>();
        proxyServersByProtocolList = proxyServers.stream().filter(proxyServer -> proxyServer.getType().equals(preferableType)).
                collect(Collectors.toList());
        if (proxyServersByProtocolList.size() != 0) {
            proxyServers = proxyServersByProtocolList;
        }
        // sort all the results by anonymity level
        ProxyServer[] proxyServersForSorting = proxyServers.toArray(new ProxyServer[proxyServers.size()]);
        mergeSorter.sort(proxyServersForSorting, proxyServers.size(), ProxyServersSortingCriterion.ANONYMITY);
        // define the number of proxy servers with the highest anonymity level (i variable)
        ProxyAnonymity theBestAnonymity = proxyServersForSorting[0].getAnonymity();
        int i = 0;
        ProxyServer proxyServer;
        do {
            if (proxyServersForSorting.length > i) {
                proxyServer = proxyServersForSorting[i];
                i++;
            } else {
                break;
            }
        } while (proxyServer.getAnonymity().equals(theBestAnonymity));
        // sort by workload only servers with the best anonymity level
        mergeSorter.sort(proxyServersForSorting, i - 1, ProxyServersSortingCriterion.WORKLOAD);
        // in case when there's several servers with the least workload, pick the one with the least uptime
        if (proxyServersForSorting.length > 1 && proxyServersForSorting[0].getWorkload().equals(
                proxyServersForSorting[1].getWorkload())) {
            List<ProxyServer> proxyServersForSortingList = new ArrayList<>();
            for (ProxyServer currentProxyServer : proxyServersForSorting) {
                if (currentProxyServer.getWorkload().equals(proxyServersForSorting[0].getWorkload())) {
                    proxyServersForSortingList.add(currentProxyServer);
                } else break;
            }
            return proxyServersForSortingList.stream().min(Comparator.comparingInt(ProxyServer::getWorkload))
                    .orElse(proxyServersForSortingList.get(0));
        }
        return proxyServersForSorting[0];
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
            logger.warn("Bad Request. POST and PUT methods must have data. Request: " + request.toString());
            return new ProxyServerResponse(null, 400,
                    "Bad Request. POST and PUT methods must have data");
        }
        if ((ContentType.JSON.equals(request.getContentType()) &&
                request.getPayload().getClass().getName().equals("java.lang.String")) ||
                (ContentType.TEXT.equals(request.getContentType()) &&
                        !request.getPayload().getClass().getName().equals("java.lang.String"))) {
            logger.warn("Bad Request. Content type doesn't correspond to payload type. Request: " +
                    request.toString());
            return new ProxyServerResponse(null, 400,
                    "Bad Request. Content type doesn't correspond to payload type");
        }
        return null;
    }

}
