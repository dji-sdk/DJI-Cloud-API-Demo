package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FirmwareVersionReceiver {

    private String firmwareVersion;

    private Integer compatibleStatus;

    private Integer firmwareUpgradeStatus;

    private String sn;

    private DeviceDomainEnum domain;
}
