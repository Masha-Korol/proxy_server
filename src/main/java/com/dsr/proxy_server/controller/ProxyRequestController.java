package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.data.dto.proxy.ProxyServerRequest;
import com.dsr.proxy_server.data.dto.proxy.ProxyServerResponse;
import com.dsr.proxy_server.service.ProxyRequestService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/requests")
public class ProxyRequestController {

    private final ProxyRequestService proxyRequestService;

    public ProxyRequestController(ProxyRequestService proxyRequestService) {
        this.proxyRequestService = proxyRequestService;
    }

    @PostMapping("/")
    public ProxyServerResponse sendRequest(@Valid @RequestBody ProxyServerRequest request) throws IOException, InterruptedException {
        return proxyRequestService.directRequestToProxyServer(request);
    }
}
