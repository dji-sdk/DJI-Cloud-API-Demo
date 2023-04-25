package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.BatteryStoreModeEnum;
import com.dji.sample.control.service.impl.RemoteDebugHandler;
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
public class BatteryStoreMode extends RemoteDebugHandler {

    private Integer action;

    @Override
    public boolean valid() {
        return Objects.nonNull(action) && BatteryStoreModeEnum.find(action).isPresent();
    }
}
