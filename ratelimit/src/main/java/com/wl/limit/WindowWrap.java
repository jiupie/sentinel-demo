package com.wl.limit;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/10/6
 */
public class WindowWrap<T> {
    /**
     * 窗口时间长度 ms
     */
    private long windowTime;

    /**
     * 窗口开始时间
     */
    private long startWindowTime;

    /**
     * 具体的值存储
     */
    private T value;


    public WindowWrap(long windowTime, long startWindowTime, T value) {
        this.windowTime = windowTime;
        this.startWindowTime = startWindowTime;
        this.value = value;
    }

    public long getWindowTime() {
        return windowTime;
    }

    public void setWindowTime(long windowTime) {
        this.windowTime = windowTime;
    }

    public long getStartWindowTime() {
        return startWindowTime;
    }

    public void setStartWindowTime(long startWindowTime) {
        this.startWindowTime = startWindowTime;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
