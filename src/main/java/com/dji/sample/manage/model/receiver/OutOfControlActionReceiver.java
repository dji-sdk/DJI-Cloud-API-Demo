package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.OsdDockDrone;
import com.dji.sdk.cloudapi.device.RcLostActionEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/3
 */
public class OutOfControlActionReceiver extends BasicDeviceProperty {

    private RcLostActionEnum rcLostAction;

    @JsonCreator
    public OutOfControlActionReceiver(Integer rcLostAction) {
        this.rcLostAction = RcLostActionEnum.find(rcLostAction);
    }

    @Override
    public boolean valid() {
        return Objects.nonNull(rcLostAction);
    }

    @Override
    public boolean canPublish(OsdDockDrone osd) {
        return rcLostAction != osd.getRcLostAction();
    }
}
