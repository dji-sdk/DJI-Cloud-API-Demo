package com.dji.sample.manage.model.dto;

import com.dji.sdk.cloudapi.device.ControlSourceEnum;
import com.dji.sdk.cloudapi.device.PayloadIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevicePayloadDTO {

    private String payloadSn;

    private String payloadName;

    private Integer index;

    private String payloadDesc;

    private ControlSourceEnum controlSource;

    private PayloadIndex payloadIndex;
}