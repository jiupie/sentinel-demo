package com.wl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/4/10
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FeignProvideApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignProvideApplication.class,args);
    }
}
