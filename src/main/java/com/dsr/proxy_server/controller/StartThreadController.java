package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.service.ProxyServerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/threads")
public class StartThreadController {

    private final ProxyServerService proxyServerService;

    public StartThreadController(ProxyServerService proxyServerService) {
        this.proxyServerService = proxyServerService;
    }

    @GetMapping("/")
    public void startThread(){
        proxyServerService.startServersCheckThread();
    }
}
