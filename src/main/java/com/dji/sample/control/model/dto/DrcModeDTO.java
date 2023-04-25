package com.dji.sample.control.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrcModeDTO {

    private MqttBrokerDTO mqttBroker;

    /**
     * range: 1 - 30
     */
    @Builder.Default
    private Integer osdFrequency = 10;

    /**
     * range: 1 - 30
     */
    @Builder.Default
    private Integer hsiFrequency = 1;
}
