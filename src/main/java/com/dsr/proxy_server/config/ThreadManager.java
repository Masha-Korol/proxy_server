package com.dsr.proxy_server.config;

import com.dsr.proxy_server.service.ProxyMaintenanceService;
import com.dsr.proxy_server.service.ProxyServersManagerService;
import com.dsr.proxy_server.thread.ServersAvailabilityUpdateThread;
import com.dsr.proxy_server.thread.ServersCheckThread;
import com.dsr.proxy_server.thread.ServersWorkloadControlThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ThreadManager {

    @Autowired
    private ProxyServersManagerService proxyServersManagerService;
    @Autowired
    private ProxyMaintenanceService proxyMaintenanceService;
    public static ServersCheckThread serversCheckThread;
    public static ServersWorkloadControlThread serversWorkloadControlThread;
    public static ServersAvailabilityUpdateThread serversAvailabilityUpdateThread;

    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                serversCheckThread = new ServersCheckThread(proxyServersManagerService);
                executor.execute(serversCheckThread);
                serversWorkloadControlThread = new ServersWorkloadControlThread(proxyMaintenanceService);
                executor.execute(serversWorkloadControlThread);
                serversAvailabilityUpdateThread = new ServersAvailabilityUpdateThread(proxyMaintenanceService);
                executor.execute(serversAvailabilityUpdateThread);
            }
        };
    }
}
