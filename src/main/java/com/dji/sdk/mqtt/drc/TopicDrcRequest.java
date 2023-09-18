package com.dji.sdk.mqtt.drc;

import com.dji.sdk.mqtt.CommonTopicRequest;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
public class TopicDrcRequest<T> extends CommonTopicRequest<T> {

    private String method;

    public TopicDrcRequest() {
    }

    @Override
    public String toString() {
        return "TopicDrcRequest{" +
                "method='" + method + '\'' +
                ", tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public TopicDrcRequest<T> setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public TopicDrcRequest<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public TopicDrcRequest<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TopicDrcRequest<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public TopicDrcRequest<T> setData(T data) {
        this.data = data;
        return this;
    }

}
