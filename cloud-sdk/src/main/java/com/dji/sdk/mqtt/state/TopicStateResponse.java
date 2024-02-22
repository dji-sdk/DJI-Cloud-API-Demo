package com.dji.sdk.mqtt.state;

import com.dji.sdk.mqtt.CommonTopicResponse;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public class TopicStateResponse<T> extends CommonTopicResponse<T> {

    public TopicStateResponse() {
    }

    @Override
    public String toString() {
        return "TopicStateResponse{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public String getTid() {
        return super.getTid();
    }

    @Override
    public TopicStateResponse<T> setTid(String tid) {
        super.setTid(tid);
        return this;
    }

    @Override
    public String getBid() {
        return super.getBid();
    }

    @Override
    public TopicStateResponse<T> setBid(String bid) {
        super.setBid(bid);
        return this;
    }

    @Override
    public T getData() {
        return super.getData();
    }

    @Override
    public TopicStateResponse<T> setData(T data) {
        super.setData(data);
        return this;
    }

    @Override
    public Long getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    public TopicStateResponse<T> setTimestamp(Long timestamp) {
        super.setTimestamp(timestamp);
        return this;
    }
}
