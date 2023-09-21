package com.dji.sample.component.mqtt.model;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/18
 */
@Getter
public enum MqttProtocolEnum {

    MQTT("tcp"),

    MQTTS("ssl"),

    WS("ws"),

    WSS("wss");

    String protocol;

    MqttProtocolEnum(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocolAddr() {
        return protocol + "://";
    }
}
