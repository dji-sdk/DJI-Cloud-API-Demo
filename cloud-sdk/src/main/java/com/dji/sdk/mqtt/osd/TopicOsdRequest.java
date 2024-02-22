package com.dji.sdk.mqtt.osd;

import com.dji.sdk.mqtt.CommonTopicRequest;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
public class TopicOsdRequest<T> extends CommonTopicRequest<T> {

    private String gateway;

    private String from;

    public TopicOsdRequest() {
    }

    @Override
    public String toString() {
        return "TopicOsdRequest{" +
                "gateway='" + gateway + '\'' +
                ", from='" + from + '\'' +
                ", tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
    public String getTid() {
        return tid;
    }

    public TopicOsdRequest<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public TopicOsdRequest<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TopicOsdRequest<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public TopicOsdRequest<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getGateway() {
        return gateway;
    }

    public TopicOsdRequest<T> setGateway(String gateway) {
        this.gateway = gateway;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public TopicOsdRequest<T> setFrom(String from) {
        this.from = from;
        return this;
    }
}
