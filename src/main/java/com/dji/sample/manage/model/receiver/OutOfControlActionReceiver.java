package com.dji.sample.manage.model.receiver;

import com.dji.sample.manage.model.enums.DroneRcLostActionEnum;
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
public class OutOfControlActionReceiver extends BasicDeviceProperty {

    private Integer value;

    @Override
    public boolean valid() {
        return Objects.nonNull(value) && value >= 0 && value < DroneRcLostActionEnum.values().length;
    }

}
