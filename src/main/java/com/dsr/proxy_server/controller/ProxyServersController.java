package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.service.ProxyServerService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/servers")
public class ProxyServersController {

    private final ProxyServerService proxyServerService;

    public ProxyServersController(ProxyServerService proxyServerService) {
        this.proxyServerService = proxyServerService;
    }

    @PostMapping("/timing")
    public void changeServersCheckTiming(@RequestBody ChangeServersCheckTimingRequest request){
        proxyServerService.changeServersCheckTiming(request);
    }
}
