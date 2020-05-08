package com.util;

/**
 * @author false
 * @date 20/4/13 19:48
 */

/**
 * 工具类，用于页面展示时填充时间
 */
public class SEtime {
    private Long startTime;
    private Long endTime;

    @Override
    public String toString() {
        return "SEtime{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public SEtime(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
