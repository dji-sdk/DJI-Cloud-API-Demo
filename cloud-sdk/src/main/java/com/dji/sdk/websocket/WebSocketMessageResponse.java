package com.dji.sdk.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * The format of WebSocket messages that the pilot can receive.
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@Schema(description = "The format of WebSocket messages that the pilot can receive.")
public class WebSocketMessageResponse<T> {

    @JsonProperty("biz_code")
    @NotNull
    @Schema(description = "webSocket messages identity", implementation = BizCodeEnum.class)
    private String bizCode;

    @Schema(description = "webSocket messages version")
    private String version = "1.0";

    @NotNull
    @Min(123456789012L)
    @Schema(description = "timestamp (milliseconds)")
    private Long timestamp;

    @NotNull
    @Schema(description = "Data corresponding to business functions")
    private T data;

    public WebSocketMessageResponse() {
    }

    @Override
    public String toString() {
        return "WebSocketMessageResponse{" +
                "bizCode=" + bizCode +
                ", version='" + version + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public String getBizCode() {
        return bizCode;
    }

    public WebSocketMessageResponse<T> setBizCode(String bizCode) {
        this.bizCode = bizCode;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public WebSocketMessageResponse<T> setVersion(String version) {
        this.version = version;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public WebSocketMessageResponse<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public WebSocketMessageResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}