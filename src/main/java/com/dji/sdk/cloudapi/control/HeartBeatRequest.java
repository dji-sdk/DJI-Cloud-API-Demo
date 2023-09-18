package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class HeartBeatRequest extends BaseModel {

    @NotNull
    private Long seq;

    @NotNull
    @Min(123456789012L)
    private Long timestamp;

    public HeartBeatRequest() {
    }

    @Override
    public String toString() {
        return "HeartBeatRequest{" +
                "seq=" + seq +
                ", timestamp=" + timestamp +
                '}';
    }

    public Long getSeq() {
        return seq;
    }

    public HeartBeatRequest setSeq(Long seq) {
        this.seq = seq;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public HeartBeatRequest setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
