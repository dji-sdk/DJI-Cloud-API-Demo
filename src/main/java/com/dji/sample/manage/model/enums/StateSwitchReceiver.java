package com.dji.sample.manage.model.enums;

import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
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
public class StateSwitchReceiver extends BasicDeviceProperty {

    public static final int DISABLE = 0;

    public static final int ENABLE = 1;

    private Integer value;

    @Override
    public boolean valid() {
        return Objects.nonNull(this.value) && (this.value == DISABLE || this.value == ENABLE);
    }
}
