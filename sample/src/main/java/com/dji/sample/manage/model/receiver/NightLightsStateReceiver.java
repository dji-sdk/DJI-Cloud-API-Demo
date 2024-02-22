package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.OsdDockDrone;
import com.dji.sdk.cloudapi.device.SwitchActionEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public class NightLightsStateReceiver extends BasicDeviceProperty {

    private SwitchActionEnum nightLightsState;

    @JsonCreator
    public NightLightsStateReceiver(Integer nightLightsState) {
        this.nightLightsState = SwitchActionEnum.find(nightLightsState);
    }

    @Override
    public boolean valid() {
        return Objects.nonNull(nightLightsState);
    }

    @Override
    public boolean canPublish(OsdDockDrone osd) {
        return nightLightsState != osd.getNightLightsState();
    }
}
