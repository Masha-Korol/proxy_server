package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationDto;
import com.dsr.proxy_server.data.dto.pagination.PageDto;
import com.dsr.proxy_server.data.dto.pagination.PageRequestDto;
import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationResponseDto;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.springframework.web.bind.annotation.*;

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
    public PageDto<ProxyServer> getAll(@RequestBody PageRequestDto pageRequest) {
        return proxyServersManagerService.getAll(pageRequest);
    }

    @PostMapping("/")
    public ProxyServerCreationResponseDto addProxyServer(@RequestBody ProxyServerCreationDto proxyServerCreationDto) {
        return proxyServersManagerService.addProxyServerByRequest(proxyServerCreationDto);
    }
}
