package com.wl.limit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/10/7
 */
public class MetricSlidWindowLimit extends SlidWindowLimit<Metric> {

    public MetricSlidWindowLimit(int timeWindows, int sampleCount) {
        super(timeWindows, sampleCount);
    }

    @Override
    protected Metric newEmptyBucket(Long currentTime) {
        return new Metric(currentTime);
    }

    @Override
    protected WindowWrap<Metric> resetWindowTo(WindowWrap<Metric> oldWindow, long startTime) {
        oldWindow.setStartWindowTime(startTime);
        Metric value = oldWindow.getValue();
        value.reset();
        return oldWindow;
    }

    public void tryAcquire() {
        long l = System.currentTimeMillis();
        List<WindowWrap<Metric>> list = this.list(l);
        long sum = 0;
        for (WindowWrap<Metric> metricWindowWrap : list) {
            sum += metricWindowWrap.getValue().getPassed();
        }
        long QPS = 3;
        LocalDateTime now = LocalDateTime.now();
        if (sum >= QPS) {
            System.out.println(now+":限流了");
        } else {
            WindowWrap<Metric> metricWindowWrap = this.currentWindow(l);
            metricWindowWrap.getValue().addPassed();
            System.out.println(now+":"+sum+":执行");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MetricSlidWindowLimit metricSlidWindowLimit = new MetricSlidWindowLimit(1000,5);
        for (int i = 0; i < 20; i++) {
            Thread.sleep(200);
            metricSlidWindowLimit.tryAcquire();
        }
    }

}
