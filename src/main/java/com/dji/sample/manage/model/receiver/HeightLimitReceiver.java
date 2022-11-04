package com.dji.sample.manage.model.receiver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeightLimitReceiver extends BasicDeviceProperty {

    private static final int HEIGHT_LIMIT_MAX = 1500;

    private static final int HEIGHT_LIMIT_MIN = 20;

    private Integer value;

    @Override
    public boolean valid() {
        return Objects.nonNull(this.value) && this.value >= HEIGHT_LIMIT_MIN && this.value <= HEIGHT_LIMIT_MAX;
    }
}
