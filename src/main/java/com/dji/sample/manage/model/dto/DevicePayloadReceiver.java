package com.dji.sample.manage.model.dto;

import com.dji.sdk.cloudapi.device.ControlSourceEnum;
import com.dji.sdk.cloudapi.device.PayloadIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicePayloadReceiver {

    private String deviceSn;

    private ControlSourceEnum controlSource;

    private PayloadIndex payloadIndex;

    private String sn;

}