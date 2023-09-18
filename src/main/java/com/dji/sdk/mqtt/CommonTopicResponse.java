package com.dji.sdk.mqtt;

/**
 * Unified Topic response format
 * @author sean.zhou
 * @date 2021/11/15
 * @version 0.1
 */
public class CommonTopicResponse<T> {

    /**
     * The command is sent and the response is matched by the tid and bid fields in the message,
     * and the reply should keep the tid and bid the same.
     */
    protected String tid;

    protected String bid;

    protected T data;

    protected Long timestamp;

    @Override
    public String toString() {
        return "CommonTopicResponse{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    public CommonTopicResponse() {
    }

    public String getTid() {
        return tid;
    }

    public CommonTopicResponse<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public CommonTopicResponse<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonTopicResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public CommonTopicResponse<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}