package com.dji.sample.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventsReceiver<T> {

    private Integer result;

    private T output;

    private String bid;

    private String sn;

}
