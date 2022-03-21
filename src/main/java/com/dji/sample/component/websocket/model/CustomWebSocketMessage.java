package com.dji.sample.component.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * The format of WebSocket messages that the pilot can receive.
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@Data
@Builder
public class CustomWebSocketMessage<T> {

    /**
     * @see BizCodeEnum
     * specific value
     */
    @JsonProperty("biz_code")
    private String bizCode;

    @Builder.Default
    private String version = "1.0";

    private Long timestamp;

    private T data;
}