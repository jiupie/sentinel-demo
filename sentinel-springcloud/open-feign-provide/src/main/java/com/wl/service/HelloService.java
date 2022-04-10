package com.wl.service;

import org.springframework.stereotype.Service;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/4/10
 */
@Service
public class HelloService {
   public String sayHello(String name) {
        return "say hello:"+name;
    }
}
