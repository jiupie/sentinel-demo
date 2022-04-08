package com.wl.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * -Dcsp.sentinel.dashboard.server=localhost:8098 -Dproject.name=sentinel-http-demo
 * @author 南顾北衫
 * @description
 * @date 2022/3/30
 */
@EnableAsync
@SpringBootApplication
public class SentinelDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentinelDemoApplication.class, args);
    }
}
