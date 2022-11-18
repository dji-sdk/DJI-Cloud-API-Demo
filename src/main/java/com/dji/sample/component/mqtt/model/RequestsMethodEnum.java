package com.dji.sample.component.mqtt.model;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/25
 */
@Getter
public enum RequestsMethodEnum {

    STORAGE_CONFIG_GET("storage_config_get", ChannelName.INBOUND_REQUESTS_STORAGE_CONFIG_GET),

    AIRPORT_BIND_STATUS("airport_bind_status", ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS),

    AIRPORT_ORGANIZATION_BIND("airport_organization_bind", ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND),

    AIRPORT_ORGANIZATION_GET("airport_organization_get", ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET),

    FLIGHT_TASK_RESOURCE_GET("flighttask_resource_get", ChannelName.INBOUND_REQUESTS_FLIGHT_TASK_RESOURCE_GET),

    CONFIG("config", ChannelName.INBOUND_REQUESTS_CONFIG),

    UNKNOWN("Unknown", ChannelName.DEFAULT);

    private String method;

    private String channelName;

    RequestsMethodEnum(String method, String channelName) {
        this.method = method;
        this.channelName = channelName;
    }

    public static RequestsMethodEnum find(String method) {
        return Arrays.stream(RequestsMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }
}
