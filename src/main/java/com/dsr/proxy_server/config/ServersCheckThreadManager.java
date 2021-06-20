package com.dsr.proxy_server.config;

import com.dsr.proxy_server.service.ProxyServersManagerService;
import com.dsr.proxy_server.thread.ServersCheckThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ServersCheckThreadManager {

    @Autowired
    private ProxyServersManagerService proxyServersManagerService;
    public static ServersCheckThread serversCheckThread;

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
            }
        };
    }
}
