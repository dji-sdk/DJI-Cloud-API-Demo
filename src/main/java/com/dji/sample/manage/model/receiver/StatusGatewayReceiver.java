package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusGatewayReceiver {

    private String sn;

    private Integer domain;

    private Integer type;

    @JsonProperty(value = "sub_type")
    private Integer subType;

    @JsonProperty(value = "device_secret")
    private String deviceSecret;

    private String nonce;

    private Integer version;

    @JsonProperty(value = "sub_devices")
    private List<StatusSubDeviceReceiver> subDevices;
}