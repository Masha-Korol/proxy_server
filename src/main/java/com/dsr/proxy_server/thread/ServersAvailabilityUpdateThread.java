package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.ProxyMaintenanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

public class ServersAvailabilityUpdateThread implements Runnable, DisposableBean {

    /**
     * This variable contains the amount of milliseconds - the time, during which this thread is sleeping
     * before next servers' check
     */
    public static Integer checkServersTimeInterval = 300000;
    private boolean isRunning = true;
    private final ProxyMaintenanceService proxyMaintenanceService;
    private static final Logger logger = LogManager.getLogger(ServersAvailabilityUpdateThread.class);

    public ServersAvailabilityUpdateThread(ProxyMaintenanceService proxyMaintenanceService) {
        this.proxyMaintenanceService = proxyMaintenanceService;
    }


    @Override
    public void run() {
        while (isRunning) {
            updateAllProxyServersAvailability();
        }
    }

    /**
     * This method updates all proxy servers' availability
     */
    private synchronized void updateAllProxyServersAvailability() {
        logger.info("thread ServersAvailabilityUpdateThread is now updating proxy servers");
        proxyMaintenanceService.updateAllProxyServersAvailability();
        try {
            logger.info("thread ServersAvailabilityUpdateThread finished updating is now waiting for "
                    + checkServersTimeInterval + " millis");
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
