package com.wl.sentinel.web;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/3/30
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        try {
            Entry entry = SphU.entry("hello");
            Node curNode = entry.getCurNode();
            System.out.println(curNode);
            return "hello";

        } catch (BlockException e) {
            e.printStackTrace();
            return "系统繁忙";
        }
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
