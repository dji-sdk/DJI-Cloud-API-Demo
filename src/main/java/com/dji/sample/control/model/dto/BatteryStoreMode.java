package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.BatteryStoreModeEnum;
import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatteryStoreMode extends BasicDeviceProperty {

    private Integer value;

    @Override
    public boolean valid() {
        return Objects.nonNull(value) && BatteryStoreModeEnum.find(value).isPresent();
    }
}
