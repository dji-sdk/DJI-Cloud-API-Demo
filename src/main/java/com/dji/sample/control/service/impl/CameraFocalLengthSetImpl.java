package com.dji.sample.control.service.impl;

import com.dji.sample.control.model.enums.CameraStateEnum;
import com.dji.sample.control.model.enums.CameraTypeEnum;
import com.dji.sample.control.model.param.DronePayloadParam;

import java.util.Objects;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public class CameraFocalLengthSetImpl extends PayloadCommandsHandler {

    public CameraFocalLengthSetImpl(DronePayloadParam param) {
        super(param);
    }

    @Override
    public boolean valid() {
        return Objects.nonNull(param.getCameraType()) && Objects.nonNull(param.getZoomFactor())
                && (CameraTypeEnum.ZOOM == param.getCameraType()
                || CameraTypeEnum.IR == param.getCameraType());
    }

    @Override
    public boolean canPublish(String deviceSn) {
        super.canPublish(deviceSn);
        if (CameraStateEnum.WORKING == osdCamera.getPhotoState()) {
            return false;
        }
        switch (param.getCameraType()) {
            case IR:
                return param.getZoomFactor().intValue() != osdCamera.getIrZoomFactor();
            case ZOOM:
                return param.getZoomFactor().intValue() != osdCamera.getZoomFactor();
        }
        return false;
    }
}
