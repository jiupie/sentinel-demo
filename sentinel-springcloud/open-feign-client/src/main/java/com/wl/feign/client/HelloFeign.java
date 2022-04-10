package com.wl.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/4/10
 */
@FeignClient(value = "sentinel-opefeign-provide", fallback = HelloFeignFallback.class)
public interface HelloFeign {

    @GetMapping("/hello")
    String hello(@RequestParam("name") String name);
}
