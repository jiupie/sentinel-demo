package com.wl.limit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 南顾北衫
 * @description 滑动窗口大小
 * @date 2022/10/6
 */
public abstract class SlidWindowLimit<T> {
    /**
     * 时间窗口大小 ms
     */
    private int timeWindows;

    /**
     * 窗口个数
     */
    private int sampleCount;

    /**
     * 间隔毫秒数
     */
    private int intervalTimeMills;

    private final AtomicReferenceArray<WindowWrap<T>> array;

    private final ReentrantLock updateLock = new ReentrantLock();

    public SlidWindowLimit(int timeWindows, int sampleCount) {
        this.timeWindows = timeWindows;
        this.sampleCount = sampleCount;
        this.intervalTimeMills = timeWindows / sampleCount;
        this.array = new AtomicReferenceArray<>(sampleCount);
    }

    public WindowWrap<T> currentWindow(Long currentTime) {
        //获取当前时间的数组索引
        int index = calcTimeIndex(currentTime);
        //获取currentTime当前开始时间
        long startTime = calcWindowStartTime(currentTime);
        while (true) {
            WindowWrap<T> oldWindow = array.get(index);

            if (oldWindow == null) {
                //数组中的值为空，说明没有使用
                WindowWrap<T> window = new WindowWrap<T>(timeWindows, startTime, newEmptyBucket(currentTime));
                if (array.compareAndSet(index, null, window)) {
                    return window;
                } else {
                    Thread.yield();
                }
            } else if (oldWindow.getStartWindowTime() == startTime) {
                //直接返回当前窗口
                return oldWindow;
            } else if (startTime > oldWindow.getStartWindowTime()) {
                //当前时间 > 索引数组的开始时间
                //说明 数组的值是老的了，可以重置了
                if (updateLock.tryLock()) {
                    try {
                        return resetWindowTo(oldWindow, startTime);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    Thread.yield();
                }
            } else if (startTime < oldWindow.getStartWindowTime()) {
                //这种情况不存在
                return new WindowWrap<T>(timeWindows, startTime, newEmptyBucket(currentTime));
            }
        }
    }

    protected abstract T newEmptyBucket(Long currentTime);

    /**
     * 获取到当前时间到 时间窗口
     *
     * @param timeMills 当前时间戳
     * @return /
     */
    public List<WindowWrap<T>> list(long timeMills) {
        int size = array.length();
        List<WindowWrap<T>> result = new ArrayList<>(size);
        //获取到所有当前时间戳到窗口
        for (int i = 0; i < size; i++) {
            WindowWrap<T> windowWrap = array.get(i);
            if (windowWrap == null || isWindowDeprecated(timeMills, windowWrap)) {
                continue;
            }
            result.add(windowWrap);
        }
        return result;
    }


    /**
     * 是否是当前时间戳到 窗口
     */
    public boolean isWindowDeprecated(long timeMills, WindowWrap<T> windowWrap) {
        return timeMills - windowWrap.getStartWindowTime() > timeWindows;
    }

    /**
     * 重置值
     */
    protected abstract WindowWrap<T> resetWindowTo(WindowWrap<T> oldWindow, long startTime);

    /**
     * 计算timeMills窗口开始时间
     *
     * @param timeMills 时间戳
     * @return timeMills时间戳对应的窗口开始时间
     */
    private long calcWindowStartTime(Long timeMills) {
        return timeMills - timeMills % intervalTimeMills;
    }

    /**
     * 获取currentTime时间在数组中的索引
     *
     * @param currentTime 时间
     * @return 索引位置
     */
    private int calcTimeIndex(Long currentTime) {
        long totalTimeCount = currentTime / intervalTimeMills;
        return (int) (totalTimeCount % array.length());
    }

}
