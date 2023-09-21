package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.OsdDockDrone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RthAltitudeReceiver extends BasicDeviceProperty {

    private Integer rthAltitude;

    private static final int RTH_ALTITUDE_MAX = 500;

    private static final int RTH_ALTITUDE_MIN = 20;

    @Override
    public boolean valid() {
        return Objects.nonNull(rthAltitude) && rthAltitude >= RTH_ALTITUDE_MIN && rthAltitude <= RTH_ALTITUDE_MAX;
    }

    @Override
    public boolean canPublish(OsdDockDrone osd) {
        return rthAltitude != osd.getRthAltitude().intValue();
    }
}
