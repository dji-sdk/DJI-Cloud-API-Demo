package com.dji.sdk.websocket;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author sean
 * @version 0.1
 * @date 2021/11/26
 */
@Schema(enumAsRef = true, description = "Pilot2 will receive these bizCode, and then do corresponding processing according to the value.")
public enum BizCodeEnum {

    DEVICE_ONLINE("device_online"),

    DEVICE_OFFLINE("device_offline"),

    DEVICE_UPDATE_TOPO("device_update_topo"),

    DEVICE_OSD("device_osd"),

    MAP_ELEMENT_CREATE("map_element_create"),

    MAP_ELEMENT_UPDATE("map_element_update"),

    MAP_ELEMENT_DELETE("map_element_delete"),

    MAP_GROUP_REFRESH("map_group_refresh");

    private final String code;

    BizCodeEnum(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }


}
