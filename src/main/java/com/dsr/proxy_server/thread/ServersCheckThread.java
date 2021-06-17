package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.ProxyServerService;

public class ServersCheckThread implements Runnable {

    private Integer checkServersTimeInterval;
    private final ProxyServerService proxyServerService;

    public ServersCheckThread(Integer checkServersTimeInterval, ProxyServerService proxyServerService) {
        this.checkServersTimeInterval = checkServersTimeInterval;
        this.proxyServerService = proxyServerService;
    }

    @Override
    public void run() {
        for (; ; ) {
         checkAllServers();
        }
    }

    private synchronized void checkAllServers(){
        System.out.println("in the cycle");
        // thread sleeps for a certain amount of seconds, then gets servers from source
        try {
            System.out.println("waiting");
            wait(checkServersTimeInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("checks threads");
        proxyServerService.checkAllServers();
    }
}
