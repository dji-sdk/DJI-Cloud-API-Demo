package com.dji.sdk.mqtt.property;

import com.dji.sdk.mqtt.CommonTopicRequest;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
public class TopicPropertySetRequest<T> extends CommonTopicRequest<T> {

    public TopicPropertySetRequest() {
    }

    @Override
    public String toString() {
        return "TopicPropertySetRequest{" +
                ", tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public String getTid() {
        return tid;
    }

    public TopicPropertySetRequest<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public TopicPropertySetRequest<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TopicPropertySetRequest<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public TopicPropertySetRequest<T> setData(T data) {
        this.data = data;
        return this;
    }

}
