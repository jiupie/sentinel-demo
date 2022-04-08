package com.wl.sentinel.web;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wl.sentinel.AsyncDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Resource
    private AsyncDemoService asyncDemoService;

    @GetMapping("/async")
    public String asyncDemo() {
        try {
            AsyncEntry asyncDemo = SphU.asyncEntry("asyncDemo");
            return asyncDemoService.async();
        } catch (BlockException e) {
            e.printStackTrace();
            return "系统繁忙";
        }
    }


    @SentinelResource(value = "anno", blockHandler = "exceptionHandler", fallback = "helloFallback")
    @GetMapping("/anno")
    public String annotionDemo() {
        return "anno";
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String helloFallback() {
        return "Halooooo";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at ";
    }


    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
