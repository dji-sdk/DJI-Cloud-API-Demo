package com.dji.sample.control.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MqttBrokerDTO {

    private String address;

    private String username;

    private String password;

    private String clientId;

    private Long expireTime;

    @Builder.Default
    private Boolean enableTls = false;
}
