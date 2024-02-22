package com.dji.sdk.cloudapi.log;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public class LogFileIndex {

    @NotNull
    private Integer bootIndex;

    @NotNull
    private Long endTime;

    @NotNull
    private Long startTime;

    @NotNull
    private Long size;

    public LogFileIndex() {
    }

    @Override
    public String toString() {
        return "LogFileIndex{" +
                "bootIndex=" + bootIndex +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", size=" + size +
                '}';
    }

    public Integer getBootIndex() {
        return bootIndex;
    }

    public LogFileIndex setBootIndex(Integer bootIndex) {
        this.bootIndex = bootIndex;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public LogFileIndex setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public LogFileIndex setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public LogFileIndex setSize(Long size) {
        this.size = size;
        return this;
    }
}
