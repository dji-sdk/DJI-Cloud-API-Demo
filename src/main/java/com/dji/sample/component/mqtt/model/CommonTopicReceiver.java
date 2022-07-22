package com.dji.sample.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Unified topic receiving format.
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTopicReceiver<T> {

    /**
     * The command is sent and the response is matched by the tid and bid fields in the message,
     * and the reply should keep the tid and bid the same.
     */
    private String tid;

    private String bid;

    private String method;

    private Long timestamp;

    private T data;

    private String gateway;

    private Integer needReply;

    private String from;
}