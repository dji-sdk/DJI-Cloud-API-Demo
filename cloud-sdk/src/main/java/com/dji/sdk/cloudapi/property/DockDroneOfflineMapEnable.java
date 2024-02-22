package com.dji.sdk.cloudapi.property;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class DockDroneOfflineMapEnable extends BaseModel {

    @JsonProperty("offline_map_enable")
    @NotNull
    private Boolean offlineMapEnable;

    public DockDroneOfflineMapEnable() {
    }

    @Override
    public String toString() {
        return "DockDroneOfflineMapEnable{" +
                "offlineMapEnable=" + offlineMapEnable +
                '}';
    }

    public Boolean getOfflineMapEnable() {
        return offlineMapEnable;
    }

    public DockDroneOfflineMapEnable setOfflineMapEnable(Boolean offlineMapEnable) {
        this.offlineMapEnable = offlineMapEnable;
        return this;
    }
}
