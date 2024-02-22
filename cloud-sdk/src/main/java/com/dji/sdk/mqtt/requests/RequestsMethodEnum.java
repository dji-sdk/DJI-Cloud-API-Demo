package com.dji.sdk.mqtt.requests;

import com.dji.sdk.cloudapi.config.RequestsConfigRequest;
import com.dji.sdk.cloudapi.flightarea.FlightAreasGetRequest;
import com.dji.sdk.cloudapi.map.OfflineMapGetRequest;
import com.dji.sdk.cloudapi.media.StorageConfigGet;
import com.dji.sdk.cloudapi.organization.AirportBindStatusRequest;
import com.dji.sdk.cloudapi.organization.AirportOrganizationBindRequest;
import com.dji.sdk.cloudapi.organization.AirportOrganizationGetRequest;
import com.dji.sdk.cloudapi.wayline.FlighttaskResourceGetRequest;
import com.dji.sdk.mqtt.ChannelName;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/25
 */
public enum RequestsMethodEnum {

    STORAGE_CONFIG_GET("storage_config_get", ChannelName.INBOUND_REQUESTS_STORAGE_CONFIG_GET, StorageConfigGet.class),

    AIRPORT_BIND_STATUS("airport_bind_status", ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS, AirportBindStatusRequest.class),

    AIRPORT_ORGANIZATION_BIND("airport_organization_bind", ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND, AirportOrganizationBindRequest.class),

    AIRPORT_ORGANIZATION_GET("airport_organization_get", ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET, AirportOrganizationGetRequest.class),

    FLIGHT_TASK_RESOURCE_GET("flighttask_resource_get", ChannelName.INBOUND_REQUESTS_FLIGHTTASK_RESOURCE_GET, FlighttaskResourceGetRequest.class),

    CONFIG("config", ChannelName.INBOUND_REQUESTS_CONFIG, RequestsConfigRequest.class),

    FLIGHT_AREAS_GET("flight_areas_get", ChannelName.INBOUND_REQUESTS_FLIGHT_AREAS_GET, FlightAreasGetRequest.class),

    OFFLINE_MAP_GET("offline_map_get", ChannelName.INBOUND_REQUESTS_OFFLINE_MAP_GET, OfflineMapGetRequest.class),

    UNKNOWN("", ChannelName.DEFAULT, Object.class);

    private final String method;

    private final String channelName;

    private final Class classType;

    RequestsMethodEnum(String method, String channelName, Class classType) {
        this.method = method;
        this.channelName = channelName;
        this.classType = classType;
    }

    public String getMethod() {
        return method;
    }

    public String getChannelName() {
        return channelName;
    }

    public Class getClassType() {
        return classType;
    }

    public static RequestsMethodEnum find(String method) {
        return Arrays.stream(RequestsMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny().orElse(UNKNOWN);
    }
}
