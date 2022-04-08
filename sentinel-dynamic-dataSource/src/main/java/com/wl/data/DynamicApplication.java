package com.wl.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * -Dcsp.sentinel.dashboard.server=localhost:8098 -Dproject.name=sentinel-http-demo
 *
 * @author 南顾北衫
 */
@SpringBootApplication
public class DynamicApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamicApplication.class, args);
    }
}
