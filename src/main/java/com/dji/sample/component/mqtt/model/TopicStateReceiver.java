package com.dji.sample.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * The data format of the state topic.
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicStateReceiver<T> {

    private String tid;

    private String bid;

    private Long timestamp;

    /**
     * The sn of the gateway device.
     */
    private String gateway;

    private T data;

}