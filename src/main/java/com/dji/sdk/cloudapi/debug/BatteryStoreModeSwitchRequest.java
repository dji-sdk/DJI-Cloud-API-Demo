package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.cloudapi.device.BatteryStoreModeEnum;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public class BatteryStoreModeSwitchRequest extends BaseModel {

    @NotNull
    private BatteryStoreModeEnum action;

    public BatteryStoreModeSwitchRequest() {
    }

    @Override
    public String toString() {
        return "BatteryStoreModeSwitchRequest{" +
                "action=" + action +
                '}';
    }

    public BatteryStoreModeEnum getAction() {
        return action;
    }

    public BatteryStoreModeSwitchRequest setAction(BatteryStoreModeEnum action) {
        this.action = action;
        return this;
    }
}
