package com.dji.sample.manage.model.receiver;

import com.dji.sample.control.model.enums.CameraModeEnum;
import com.dji.sample.control.model.enums.CameraStateEnum;
import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/8
 */
@Data
public class OsdCameraReceiver {

    private CameraModeEnum cameraMode;

    private LiveviewWorldRegionReceiver liveviewWorldRegion;

    private String payloadIndex;

    private CameraStateEnum photoState;

    private Integer recordTime;

    private CameraStateEnum recordingState;

    private Long remainPhotoNum;

    private Long remainRecordDuration;

    private Float zoomFactor;

    private Float irZoomFactor;
}
