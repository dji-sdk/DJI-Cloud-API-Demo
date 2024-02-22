package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.cloudapi.device.SimSlotEnum;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class SimSlotSwitchRequest extends BaseModel {

    /**
     * Identifies the dongle to be operated on.
     */
    @NotNull
    private String imei;

    /**
     * Identifies the target device to operate on.
     */
    @NotNull
    private DongleDeviceTypeEnum deviceType;

    /**
     * Switch between using physical sim card and using esim.
     */
    @NotNull
    private SimSlotEnum simSlot;

    public SimSlotSwitchRequest() {
    }

    @Override
    public String toString() {
        return "SimSlotSwitchRequest{" +
                "imei='" + imei + '\'' +
                ", deviceType=" + deviceType +
                ", simSlot=" + simSlot +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public SimSlotSwitchRequest setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public DongleDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public SimSlotSwitchRequest setDeviceType(DongleDeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public SimSlotEnum getSimSlot() {
        return simSlot;
    }

    public SimSlotSwitchRequest setSimSlot(SimSlotEnum simSlot) {
        this.simSlot = simSlot;
        return this;
    }
}
