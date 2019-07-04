package com.abc.monitor_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.abc.monitor_server.controller", "com.abc.monitor_server.service", "com.abc.monitor_server.config"})
@MapperScan({"com.abc.monitor_server.mapper", "com.abc.monitor_server.mybatis.mapper"})
@EnableScheduling
public class MonitorServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorServerApplication.class, args);
    }

}
