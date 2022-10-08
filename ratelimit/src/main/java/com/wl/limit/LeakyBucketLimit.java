package com.wl.limit;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/10/8
 */
public class LeakyBucketLimit {
    private AtomicLong latestPassedTime = new AtomicLong(-1);

    private long timout;

    private long qps;
    private long cost;

    public LeakyBucketLimit(long timout, long qps) {
        this.timout = timout;
        this.qps = qps;
        this.cost = (long) ((1.0 / qps) * 1000);
    }

    public  boolean tryAcquire() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();

        long l = latestPassedTime.get() + cost;
        if (l <= currentTimeMillis) {
            this.latestPassedTime.set(System.currentTimeMillis());
            return true;
        } else {
            //上次执行请求时间+本次请求耗费时间-当前时间  是否超时
            long requestTimout = latestPassedTime.get() + cost - System.currentTimeMillis();
            if (requestTimout > timout) {
                return false;
            } else {
                long l1 = latestPassedTime.get() + cost - System.currentTimeMillis();
                if (l1 > 0) {
                    //更新
                    latestPassedTime.addAndGet(cost);
                    Thread.sleep(l1);
                }
                return true;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LeakyBucketLimit leakyBucketLimit = new LeakyBucketLimit(1000, 1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                LocalDateTime now = LocalDateTime.now();
                try {
                    System.out.println(now + ":" + leakyBucketLimit.tryAcquire());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }

    }
}
