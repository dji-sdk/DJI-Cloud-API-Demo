package com.dji.sdk.mqtt;

/**
 * Unified topic request format.
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public class CommonTopicRequest<T> {

    /**
     * The command is sent and the response is matched by the tid and bid fields in the message,
     * and the reply should keep the tid and bid the same.
     */
    protected String tid;

    protected String bid;

    protected Long timestamp;

    protected T data;

    public CommonTopicRequest() {
    }

    @Override
    public String toString() {
        return "CommonTopicRequest{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public String getTid() {
        return tid;
    }

    public CommonTopicRequest<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public CommonTopicRequest<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public CommonTopicRequest<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonTopicRequest<T> setData(T data) {
        this.data = data;
        return this;
    }
}