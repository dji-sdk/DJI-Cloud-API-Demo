package com.dji.sample.manage.model.receiver;

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

    private Integer value;

    private static final int RTH_ALTITUDE_MAX = 500;

    private static final int RTH_ALTITUDE_MIN = 20;

    @Override
    public boolean valid() {
        return  Objects.nonNull(this.value) && this.value >= RTH_ALTITUDE_MIN && this.value <= RTH_ALTITUDE_MAX;
    }

}
