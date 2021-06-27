package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;

/**
 * This thread nullifies every server's workload once in a time interval (nullifyWorkloadTimeInterval)
 */
@Scope("prototype")
public class ServersWorkloadControlThread implements Runnable, DisposableBean {

    /**
     * This variable contains the amount of milliseconds - the time, during which this thread is sleeping
     * before next servers' workloads' nullifying
     */
    public static Integer nullifyWorkloadTimeInterval = 3600000 * 24;
    private boolean isRunning = true;
    private final ProxyServersManagerService proxyServersManagerService;
    private static final Logger logger = LogManager.getLogger(ServersWorkloadControlThread.class);

    public ServersWorkloadControlThread(ProxyServersManagerService proxyServersManagerService) {
        this.proxyServersManagerService = proxyServersManagerService;
    }

    @Override
    public void run() {
        while (isRunning) {
            nullifyWorkload();
        }
    }

    private synchronized void nullifyWorkload() {
        logger.info("started nullifying proxy's workload");
        proxyServersManagerService.nullifyWorkload();
        try {
            wait(nullifyWorkloadTimeInterval);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        isRunning = false;
        logger.warn("ServersWorkloadControlThread bean was destroyed and the thread stopped working");
    }
}
