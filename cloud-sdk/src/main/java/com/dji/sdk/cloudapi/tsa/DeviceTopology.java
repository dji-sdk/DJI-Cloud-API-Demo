package com.dji.sdk.cloudapi.tsa;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Schema(description = "device topology data")
public class DeviceTopology {

    @NotNull
    @Schema(description = "device sn", example = "1AEC32CK4AD23R")
    private String sn;

    @NotNull
    @JsonProperty("device_callsign")
    @Schema(description = "device nickname", example = "my M350")
    private String deviceCallsign;

    @NotNull
    @JsonProperty("device_model")
    @Valid
    private TopologyDeviceModel deviceModel;

    @NotNull
    @Schema(description = "online status")
    @JsonProperty("online_status")
    private Boolean onlineStatus;

    @Schema(description = "the id of the person using the device", format = "uuid")
    @JsonProperty("user_id")
    private String userId;

    @NotNull
    @Schema(description = "the nickname of the person using the device", example = "admin")
    @JsonProperty("user_callsign")
    private String userCallsign;

    @NotNull
    @JsonProperty("icon_urls")
    @Valid
    private DeviceIconUrl iconUrls;

    public DeviceTopology() {
    }

    @Override
    public String toString() {
        return "DeviceTopology{" +
                "sn='" + sn + '\'' +
                ", deviceCallsign='" + deviceCallsign + '\'' +
                ", deviceModel=" + deviceModel +
                ", onlineStatus=" + onlineStatus +
                ", userId='" + userId + '\'' +
                ", userCallsign='" + userCallsign + '\'' +
                ", iconUrls=" + iconUrls +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public DeviceTopology setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getDeviceCallsign() {
        return deviceCallsign;
    }

    public DeviceTopology setDeviceCallsign(String deviceCallsign) {
        this.deviceCallsign = deviceCallsign;
        return this;
    }

    public TopologyDeviceModel getDeviceModel() {
        return deviceModel;
    }

    public DeviceTopology setDeviceModel(TopologyDeviceModel deviceModel) {
        this.deviceModel = deviceModel;
        return this;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public DeviceTopology setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public DeviceTopology setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserCallsign() {
        return userCallsign;
    }

    public DeviceTopology setUserCallsign(String userCallsign) {
        this.userCallsign = userCallsign;
        return this;
    }

    public DeviceIconUrl getIconUrls() {
        return iconUrls;
    }

    public DeviceTopology setIconUrls(DeviceIconUrl iconUrls) {
        this.iconUrls = iconUrls;
        return this;
    }
}
