package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OsdGatewayReceiver {

    private Double latitude;

    private Double longitude;

    @JsonProperty("capacity_percent")
    private Integer remainPower;

    private Integer transmissionSignalQuality;

    private LiveStatusReceiver liveStatus;

    private WirelessLinkStateReceiver wirelessLinkState;
}