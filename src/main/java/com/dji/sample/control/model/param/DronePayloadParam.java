package com.dji.sample.control.model.param;

import com.dji.sdk.cloudapi.control.CameraTypeEnum;
import com.dji.sdk.cloudapi.control.GimbalResetModeEnum;
import com.dji.sdk.cloudapi.device.CameraModeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
@Data
public class DronePayloadParam {

    @Pattern(regexp = "\\d+-\\d+-\\d+")
    @NotNull
    private String payloadIndex;

    private CameraTypeEnum cameraType;

    @Range(min = 2, max = 200)
    private Float zoomFactor;

    private CameraModeEnum cameraMode;

    /**
     * true: Lock the gimbal, the gimbal and the drone rotate together.
     * false: Only the gimbal rotates, but the drone does not.
     */
    private Boolean locked;

    private Double pitchSpeed;

    /**
     * Only valid when locked is false.
     */
    private Double yawSpeed;

    /**
     * upper left corner as center point
     */
    @Range(min = 0, max = 1)
    private Double x;

    @Range(min = 0, max = 1)
    private Double y;

    private GimbalResetModeEnum resetMode;
}
