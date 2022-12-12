package com.dji.sample.manage.model.receiver;

import com.dji.sample.manage.model.enums.StateSwitchEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NightLightsStateReceiver extends BasicDeviceProperty {

    private Integer value;

    @Override
    public boolean valid() {
        return Objects.nonNull(value) && StateSwitchEnum.find(value).isPresent();
    }
}
