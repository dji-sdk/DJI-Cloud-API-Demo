package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/6
 */
@Data
public class OsdPayloadReceiver {

    private String payloadIndex;

    private Double gimbalPitch;

    private Double gimbalRoll;

    private Double gimbalYaw;

    private Double measureTargetAltitude;

    private Double measureTargetDistance;

    private Double measureTargetLatitude;

    private Double measureTargetLongitude;

    private Integer measureTargetErrorState;

    private Integer version;

}
