package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/12
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusSubDeviceReceiver {

    private String sn;

    private Integer domain;

    private Integer type;

    @JsonProperty(value = "sub_type")
    private Integer subType;

    private String index;

    @JsonProperty(value = "device_secret")
    private String deviceSecret;

    private String nonce;

    private Integer version;
}