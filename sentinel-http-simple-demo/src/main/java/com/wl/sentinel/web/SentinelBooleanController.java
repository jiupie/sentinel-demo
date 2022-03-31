package com.wl.sentinel.web;

import com.alibaba.csp.sentinel.SphO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/3/31
 */
@RestController
public class SentinelBooleanController {

    @GetMapping("/boolean")
    public boolean sentinelBoolean(){
        if(SphO.entry("sentinel_bool")){
            System.out.println("正常");
//            SphO.exit();
            return true;
        }else {
            System.out.println("被限流");
            return false;
        }
    }
}
