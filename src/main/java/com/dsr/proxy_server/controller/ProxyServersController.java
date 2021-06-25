package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/servers")
public class ProxyServersController {

    private final ProxyServersManagerService proxyServersManagerService;

    public ProxyServersController(ProxyServersManagerService proxyServersManagerService) {
        this.proxyServersManagerService = proxyServersManagerService;
    }

    @PostMapping("/timing")
    public void changeServersCheckTiming(@RequestBody ChangeServersCheckTimingRequest request) {
        proxyServersManagerService.changeServersCheckTiming(request);
    }

    @GetMapping("/")
    public List<ProxyServer> getAll() {
        return proxyServersManagerService.getAll();
    }
}
