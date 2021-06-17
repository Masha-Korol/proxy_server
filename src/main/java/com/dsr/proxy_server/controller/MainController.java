package com.dsr.proxy_server.controller;

import com.dsr.proxy_server.data.dto.ProxyServerResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/main")
public class MainController {

    private final RestTemplate restTemplate;

    public MainController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public ProxyServerResponseEntity getAll(){
        String url = "http://api.foxtools.ru/v2/Proxy";
        ProxyServerResponseEntity forObject = restTemplate.getForObject(url, ProxyServerResponseEntity.class);
        return forObject;
    }
}
