package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.CountryService;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("thread")
@Scope("prototype")
public class ServersCheckThread implements Runnable {

    public static Integer checkServersTimeInterval = 10000;
    private final ProxyServersManagerService proxyServersManagerService;
    private static final Logger logger = LogManager.getLogger(ServersCheckThread.class);

    public ServersCheckThread(ProxyServersManagerService proxyServersManagerService) {
        this.proxyServersManagerService = proxyServersManagerService;
    }

    @Override
    public void run() {
        for (; ; ) {
            checkAllServers();
        }
    }

    private synchronized void checkAllServers() {
        logger.info("checks threads");
        // thread gets servers from source
        // then sleeps for a certain amount of time (checkServersTimeInterval variable)
        proxyServersManagerService.checkAllServers();
        try {
            logger.info("waiting for " + checkServersTimeInterval + " millis");
            wait(checkServersTimeInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
