package com.dji.sdk.cloudapi.tsa;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.3
 * @date 2022/1/5
 */
@Schema(description = "device icon url. <br/>You can use icons from the web, and the App internally downloads and caches these icons and" +
        " loads them at a fixed size (28dp) to display on the map. Example: http://r56978dr7.hn-bkt.clouddn.com/tsa_equipment_normal.png",
        anyOf = IconUrlEnum.class)
public class DeviceIconUrl {

    @JsonProperty("normal_icon_url")
    @NotNull
    @Schema(description = "icon displayed in normal state", example = "resource://pilot/drawable/tsa_car_normal")
    private String normalIconUrl;

    @JsonProperty("selected_icon_url")
    @NotNull
    @Schema(description = "icon displayed in selected state", example = "resource://pilot/drawable/tsa_car_select")
    private String selectIconUrl;

    public DeviceIconUrl() {
    }

    @Override
    public String toString() {
        return "DeviceIconUrl{" +
                "normalIconUrl='" + normalIconUrl + '\'' +
                ", selectIconUrl='" + selectIconUrl + '\'' +
                '}';
    }

    public String getNormalIconUrl() {
        return normalIconUrl;
    }

    public DeviceIconUrl setNormalIconUrl(String normalIconUrl) {
        this.normalIconUrl = normalIconUrl;
        return this;
    }

    public String getSelectIconUrl() {
        return selectIconUrl;
    }

    public DeviceIconUrl setSelectIconUrl(String selectIconUrl) {
        this.selectIconUrl = selectIconUrl;
        return this;
    }
}
