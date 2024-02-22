package com.dji.sdk.cloudapi.control;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.device.ExitWaylineWhenRcLostEnum;
import com.dji.sdk.cloudapi.device.RcLostActionEnum;
import com.dji.sdk.cloudapi.wayline.RthModeEnum;
import com.dji.sdk.cloudapi.wayline.SimulateMission;
import com.dji.sdk.common.BaseModel;
import com.dji.sdk.config.version.CloudSDKVersionEnum;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public class TakeoffToPointRequest extends BaseModel {

    @Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$")
    @NotNull
    private String flightId;

    @Min(-180)
    @Max(180)
    @NotNull
    private Float targetLongitude;

    @Min(-90)
    @Max(90)
    @NotNull
    private Float targetLatitude;

    @Min(2)
    @Max(10000)
    @NotNull
    private Float targetHeight;

    @Min(20)
    @Max(1500)
    @NotNull
    private Float securityTakeoffHeight;

    @Min(2)
    @Max(1500)
    @NotNull
    private Float rthAltitude;

    @NotNull
    private RcLostActionEnum rcLostAction;

    @NotNull
    @CloudSDKVersion(deprecated = CloudSDKVersionEnum.V1_0_0)
    private ExitWaylineWhenRcLostEnum exitWaylineWhenRcLost;

    @Min(1)
    @Max(15)
    @NotNull
    private Integer maxSpeed;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @NotNull
    private RthModeEnum rthMode;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @NotNull
    private CommanderModeLostActionEnum commanderModeLostAction;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @NotNull
    private CommanderFlightModeEnum commanderFlightMode;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @NotNull
    @Min(2)
    @Max(3000)
    private Float commanderFlightHeight;

    @Valid
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private SimulateMission simulateMission;

    public TakeoffToPointRequest() {
    }

    @Override
    public String toString() {
        return "TakeoffToPointRequest{" +
                "flightId='" + flightId + '\'' +
                ", targetLongitude=" + targetLongitude +
                ", targetLatitude=" + targetLatitude +
                ", targetHeight=" + targetHeight +
                ", securityTakeoffHeight=" + securityTakeoffHeight +
                ", rthAltitude=" + rthAltitude +
                ", rcLostAction=" + rcLostAction +
                ", exitWaylineWhenRcLost=" + exitWaylineWhenRcLost +
                ", maxSpeed=" + maxSpeed +
                ", rthMode=" + rthMode +
                ", commanderModeLostAction=" + commanderModeLostAction +
                ", commanderFlightMode=" + commanderFlightMode +
                ", commanderFlightHeight=" + commanderFlightHeight +
                ", simulateMission=" + simulateMission +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public TakeoffToPointRequest setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }

    public Float getTargetLongitude() {
        return targetLongitude;
    }

    public TakeoffToPointRequest setTargetLongitude(Float targetLongitude) {
        this.targetLongitude = targetLongitude;
        return this;
    }

    public Float getTargetLatitude() {
        return targetLatitude;
    }

    public TakeoffToPointRequest setTargetLatitude(Float targetLatitude) {
        this.targetLatitude = targetLatitude;
        return this;
    }

    public Float getTargetHeight() {
        return targetHeight;
    }

    public TakeoffToPointRequest setTargetHeight(Float targetHeight) {
        this.targetHeight = targetHeight;
        return this;
    }

    public Float getSecurityTakeoffHeight() {
        return securityTakeoffHeight;
    }

    public TakeoffToPointRequest setSecurityTakeoffHeight(Float securityTakeoffHeight) {
        this.securityTakeoffHeight = securityTakeoffHeight;
        return this;
    }

    public Float getRthAltitude() {
        return rthAltitude;
    }

    public TakeoffToPointRequest setRthAltitude(Float rthAltitude) {
        this.rthAltitude = rthAltitude;
        return this;
    }

    public RcLostActionEnum getRcLostAction() {
        return rcLostAction;
    }

    public TakeoffToPointRequest setRcLostAction(RcLostActionEnum rcLostAction) {
        this.rcLostAction = rcLostAction;
        return this;
    }

    public ExitWaylineWhenRcLostEnum getExitWaylineWhenRcLost() {
        return exitWaylineWhenRcLost;
    }

    public TakeoffToPointRequest setExitWaylineWhenRcLost(ExitWaylineWhenRcLostEnum exitWaylineWhenRcLost) {
        this.exitWaylineWhenRcLost = exitWaylineWhenRcLost;
        return this;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public RthModeEnum getRthMode() {
        return rthMode;
    }

    public TakeoffToPointRequest setRthMode(RthModeEnum rthMode) {
        this.rthMode = rthMode;
        return this;
    }

    public CommanderModeLostActionEnum getCommanderModeLostAction() {
        return commanderModeLostAction;
    }

    public TakeoffToPointRequest setCommanderModeLostAction(CommanderModeLostActionEnum commanderModeLostAction) {
        this.commanderModeLostAction = commanderModeLostAction;
        return this;
    }

    public CommanderFlightModeEnum getCommanderFlightMode() {
        return commanderFlightMode;
    }

    public TakeoffToPointRequest setCommanderFlightMode(CommanderFlightModeEnum commanderFlightMode) {
        this.commanderFlightMode = commanderFlightMode;
        return this;
    }

    public Float getCommanderFlightHeight() {
        return commanderFlightHeight;
    }

    public TakeoffToPointRequest setCommanderFlightHeight(Float commanderFlightHeight) {
        this.commanderFlightHeight = commanderFlightHeight;
        return this;
    }

    public TakeoffToPointRequest setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public SimulateMission getSimulateMission() {
        return simulateMission;
    }

    public TakeoffToPointRequest setSimulateMission(SimulateMission simulateMission) {
        this.simulateMission = simulateMission;
        return this;
    }
}
