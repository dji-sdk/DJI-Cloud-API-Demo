package com.dji.sample.control.model.dto;

import com.dji.sample.control.service.impl.RemoteDebugHandler;
import com.dji.sdk.cloudapi.device.LinkWorkModeEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LinkWorkMode extends RemoteDebugHandler {

    private LinkWorkModeEnum linkWorkMode;

    @JsonCreator
    public LinkWorkMode(@JsonProperty("action") Integer linkWorkMode) {
        this.linkWorkMode = LinkWorkModeEnum.find(linkWorkMode);
    }

    @JsonValue
    public Map toMap() {
        return Map.of("link_workmode", linkWorkMode.getMode());
    }
}
