package com.wl.feign.web;

import com.wl.dubbo.provide.SayProvide;
import com.wl.feign.client.HelloFeign;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/4/10
 */
@RestController
public class HelloRest {
    @Value("${server.port}")
    private Integer port;

    @DubboReference(mock = "")
    private SayProvide sayProvide;

    @Autowired
    HelloFeign helloFeign;

    @GetMapping("/h")
    public String hello(String name) {
        if ("error".equals(name)) {
            throw new RuntimeException("error");
        }
        return helloFeign.hello(name + "(" + port + ")" + "【" + sayProvide.sayChinese(name) + "】");
    }
}
