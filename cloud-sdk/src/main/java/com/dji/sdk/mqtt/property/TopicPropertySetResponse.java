package com.dji.sdk.mqtt.property;

import com.dji.sdk.mqtt.CommonTopicResponse;

/**
 * Unified Topic request format
 * @author sean.zhou
 * @date 2021/11/15
 * @version 0.1
 */
public class TopicPropertySetResponse<T> extends CommonTopicResponse<T> {

    @Override
    public String toString() {
        return "TopicPropertySetResponse{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    public TopicPropertySetResponse() {
    }

    public String getTid() {
        return tid;
    }

    public TopicPropertySetResponse<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public TopicPropertySetResponse<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public T getData() {
        return data;
    }

    public TopicPropertySetResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TopicPropertySetResponse<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}