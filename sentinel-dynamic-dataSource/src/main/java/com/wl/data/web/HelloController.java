package com.wl.data.web;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 南顾北衫
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        try {
            SphU.entry("TestResource");
            return "hello";
        } catch (BlockException e) {
            e.printStackTrace();
            return "请求繁忙";
        }
    }
}
