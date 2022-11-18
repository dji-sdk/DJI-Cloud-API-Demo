package com.dji.sample.component.mqtt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventsReceiver<T> {

    private Integer result;

    private T output;

    private String bid;

    private String sn;

}
