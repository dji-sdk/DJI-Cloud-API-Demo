package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.DockDistanceLimitStatus;
import com.dji.sdk.cloudapi.device.OsdDockDrone;
import com.dji.sdk.cloudapi.device.SwitchActionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceLimitStatusReceiver extends BasicDeviceProperty {

    private SwitchActionEnum state;

    private Integer distanceLimit;

    private static final int DISTANCE_MAX = 8000;

    private static final int DISTANCE_MIN = 15;

    @Override
    public boolean valid() {
        if (Objects.isNull(state) && Objects.isNull(distanceLimit)) {
            return false;
        }
        if (Objects.nonNull(distanceLimit)) {
            return distanceLimit >= DISTANCE_MIN && distanceLimit <= DISTANCE_MAX;
        }
        return true;
    }

    @Override
    public boolean canPublish(OsdDockDrone osd) {
        DockDistanceLimitStatus distanceLimitStatus = osd.getDistanceLimitStatus();
        return (Objects.nonNull(distanceLimitStatus.getState()) && distanceLimitStatus.getState() != this.state)
                || (Objects.nonNull(distanceLimitStatus.getDistanceLimit())
                        && distanceLimitStatus.getDistanceLimit().intValue() != this.distanceLimit);
    }
}
