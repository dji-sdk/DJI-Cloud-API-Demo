package com.dji.sample.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified Topic response format
 * @author sean.zhou
 * @date 2021/11/15
 * @version 0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTopicResponse<T> {

    /**
     * The command is sent and the response is matched by the tid and bid fields in the message,
     * and the reply should keep the tid and bid the same.
     */
    private String tid;

    private String bid;

    private String method;

    private T data;

    private Long timestamp;
}