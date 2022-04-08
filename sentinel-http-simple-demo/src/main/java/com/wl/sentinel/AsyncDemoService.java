package com.wl.sentinel;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 南顾北衫
 */
@Service
public class AsyncDemoService {

    @Async
    public String async()     {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("async demo");
        return "async demo";
    }
}
