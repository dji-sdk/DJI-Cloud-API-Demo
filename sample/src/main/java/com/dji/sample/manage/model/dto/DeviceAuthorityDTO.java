package com.dji.sample.manage.model.dto;

import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sdk.cloudapi.device.ControlSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/2
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAuthorityDTO {

    private String sn;

    private DroneAuthorityEnum type;

    private ControlSourceEnum controlSource;

}
