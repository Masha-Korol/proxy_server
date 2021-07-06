package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationDto;
import com.dsr.proxy_server.data.dto.pagination.PageDto;
import com.dsr.proxy_server.data.dto.pagination.PageRequestDto;
import com.dsr.proxy_server.data.dto.proxy_creation.ProxyServerCreationResponseDto;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.service.ProxyMaintenanceService;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/servers")
public class ProxyServersController {

    private final ProxyServersManagerService proxyServersManagerService;
    private final ProxyMaintenanceService proxyMaintenanceService;

    public ProxyServersController(ProxyServersManagerService proxyServersManagerService, ProxyMaintenanceService proxyMaintenanceService) {
        this.proxyServersManagerService = proxyServersManagerService;
        this.proxyMaintenanceService = proxyMaintenanceService;
    }

    @PostMapping("/timing")
    public void changeServersCheckTiming(@Valid @RequestBody ChangeServersCheckTimingRequest request) {
        proxyMaintenanceService.changeServersCheckTiming(request);
    }

    @GetMapping("/")
    public PageDto<ProxyServer> getAll(@Valid @RequestBody PageRequestDto pageRequest) {
        return proxyServersManagerService.getAll(pageRequest);
    }

    @PostMapping("/")
    public ProxyServerCreationResponseDto addProxyServer(@Valid @RequestBody ProxyServerCreationDto proxyServerCreationDto) {
        return proxyServersManagerService.addProxyServerByRequest(proxyServerCreationDto);
    }
}
