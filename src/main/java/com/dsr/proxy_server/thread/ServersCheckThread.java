package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.ProxyServerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("thread")
@Scope("prototype")
public class ServersCheckThread implements Runnable {

    public static Integer checkServersTimeInterval = 10000;
    private final ProxyServerService proxyServerService;

    public ServersCheckThread(ProxyServerService proxyServerService) {
        this.proxyServerService = proxyServerService;
    }

    @Override
    public void run() {
        for (; ; ) {
            checkAllServers();
        }
    }

    private synchronized void checkAllServers() {
        System.out.println("checks threads");
        System.out.println("timing = " + checkServersTimeInterval + " millis");
        // thread gets servers from source then sleeps for a certain amount of time
        proxyServerService.checkAllServers();
        try {
            System.out.println("waiting");
            wait(checkServersTimeInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
