package com.wl.feign.client;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/4/10
 */
@Component
public class HelloFeignFallback implements HelloFeign {

    @Override
    public String hello(String name) {
        return "请求失败：" ;
    }
}
