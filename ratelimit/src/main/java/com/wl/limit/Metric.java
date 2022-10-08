package com.wl.limit;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/10/7
 */
public class Metric {

    /**
     * 请求时间
     */
    private long requestTimeMills;

    private LongAdder passed;

    public Metric(long requestTimeMills) {
        this.requestTimeMills = requestTimeMills;
        this.passed = new LongAdder();
    }

    public long getRequestTimeMills() {
        return requestTimeMills;
    }

    public void addPassed() {
        passed.increment();
    }

    public void reset() {
        passed.reset();
    }

    public long getPassed() {
        return passed.sum();
    }
}
