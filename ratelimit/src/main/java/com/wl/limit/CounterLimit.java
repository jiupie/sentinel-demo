package com.wl.limit;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author 南顾北衫
 * @description 固定窗口限流
 * @date 2022/10/6
 */
public class CounterLimit {
    /**
     * 每秒QPS
     **/
    private static final Integer QPS = 2;

    /**
     * 时间窗口 ms
     */
    private static final Integer TIME_WINDOWS = 1000;

    /**
     * 计数器
     */
    private static LongAdder COUNT = new LongAdder();

    /**
     * 开始时间
     */
    private static Long START_TIME = System.currentTimeMillis();

    public static synchronized boolean tryAcquire() {
        long current = System.currentTimeMillis();
        if (current - START_TIME > TIME_WINDOWS) {
            START_TIME = System.currentTimeMillis();
            COUNT.reset();
        } else {
            if (COUNT.sum() > QPS - 1) {
                return false;
            } else {
                COUNT.increment();
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                boolean b = CounterLimit.tryAcquire();
                LocalTime now = LocalTime.now();
                if (!b) {
                    System.out.println(now + "：被限流了");
                } else {
                    System.out.println(now + "：通行");
                }
            });
        }
    }
}
