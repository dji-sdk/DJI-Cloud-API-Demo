package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.OsdDockDrone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeightLimitReceiver extends BasicDeviceProperty {

    private static final int HEIGHT_LIMIT_MAX = 1500;

    private static final int HEIGHT_LIMIT_MIN = 20;

    private Integer heightLimit;

    @Override
    public boolean valid() {
        return Objects.nonNull(this.heightLimit) && this.heightLimit >= HEIGHT_LIMIT_MIN && this.heightLimit <= HEIGHT_LIMIT_MAX;
    }

    @Override
    public boolean canPublish(OsdDockDrone osd) {
        return heightLimit.intValue() != osd.getHeightLimit();
    }
}
