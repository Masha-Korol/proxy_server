package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.CountryService;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * This thread checks all the proxy servers, adds servers, if they're not in the db
 * and marks them available or unavailable if their status has changed
 */
@Scope("prototype")
public class ServersCheckThread implements Runnable, DisposableBean {

    /**
     * This variable contains the amount of milliseconds - the time, during which this thread is sleeping
     * before next servers' check
     */
    public static Integer checkServersTimeInterval = 3600000;
    private boolean isRunning = true;
    private final ProxyServersManagerService proxyServersManagerService;
    private static final Logger logger = LogManager.getLogger(ServersCheckThread.class);

    public ServersCheckThread(ProxyServersManagerService proxyServersManagerService) {
        this.proxyServersManagerService = proxyServersManagerService;
    }

    @Override
    public void run() {
        while (isRunning) {
            checkAllServers();
        }
    }


    /**
     * This method calls method, that checks all the servers (adds and deletes them if needed), then sleeps for a certain amount of time
     */
    private synchronized void checkAllServers() {
        logger.info("thread ServersCheckThread is now checking proxy servers");
        // thread gets servers from source
        // then sleeps for a certain amount of time (checkServersTimeInterval variable)
        proxyServersManagerService.checkAllServers();
        try {
            logger.info("thread ServersCheckThread is now waiting for " + checkServersTimeInterval + " millis");
            wait(checkServersTimeInterval);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        isRunning = false;
        logger.warn("ServersCheckThread bean was destroyed and the thread stopped working");
    }
}
