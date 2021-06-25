package com.dsr.proxy_server.thread;

import com.dsr.proxy_server.service.ProxyServersManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;

/**
 * This thread nullifies every server's workload once in a time interval (nullifyWorkloadTimeInterval)
 */
@Scope("prototype")
public class ServersWorkloadControlThread implements Runnable {

    /**
     * This variable contains the amount of milliseconds - the time, during which this thread is sleeping
     * before next servers' workloads' nullifying
     */
    public static Integer nullifyWorkloadTimeInterval = 3600000 * 24;
    private final ProxyServersManagerService proxyServersManagerService;
    private static final Logger logger = LogManager.getLogger(ServersWorkloadControlThread.class);

    public ServersWorkloadControlThread(ProxyServersManagerService proxyServersManagerService) {
        this.proxyServersManagerService = proxyServersManagerService;
    }

    @Override
    public void run() {
        for (; ; ) {
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
}
