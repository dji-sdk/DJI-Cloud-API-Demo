package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.cloudapi.device.TelecomOperatorEnum;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class EsimOperatorSwitchRequest extends BaseModel {

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
     * Target carrier for switching.
     */
    @NotNull
    private TelecomOperatorEnum telecomOperator;

    public EsimOperatorSwitchRequest() {
    }

    @Override
    public String toString() {
        return "EsimOperatorSwitchRequest{" +
                "imei='" + imei + '\'' +
                ", deviceType=" + deviceType +
                ", telecomOperator=" + telecomOperator +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public EsimOperatorSwitchRequest setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public DongleDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public EsimOperatorSwitchRequest setDeviceType(DongleDeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public TelecomOperatorEnum getTelecomOperator() {
        return telecomOperator;
    }

    public EsimOperatorSwitchRequest setTelecomOperator(TelecomOperatorEnum telecomOperator) {
        this.telecomOperator = telecomOperator;
        return this;
    }
}
