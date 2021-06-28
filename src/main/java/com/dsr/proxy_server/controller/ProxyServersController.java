package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.config.ThreadManager;
import com.dsr.proxy_server.data.dto.ChangeServersCheckTimingRequest;
import com.dsr.proxy_server.data.dto.pagination.PageDto;
import com.dsr.proxy_server.data.dto.pagination.PageRequestDto;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/servers")
public class ProxyServersController {

    private final ProxyServersManagerService proxyServersManagerService;
    @Autowired
    private ApplicationContext context;

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
}
