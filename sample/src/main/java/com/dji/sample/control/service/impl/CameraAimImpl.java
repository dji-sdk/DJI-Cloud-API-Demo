package com.dji.sample.control.service.impl;

import com.dji.sample.control.model.param.DronePayloadParam;

import java.util.Objects;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public class CameraAimImpl extends PayloadCommandsHandler {

    public CameraAimImpl(DronePayloadParam param) {
        super(param);
    }

    @Override
    public boolean valid() {
        return Objects.nonNull(param.getX()) && Objects.nonNull(param.getY())
                && Objects.nonNull(param.getLocked()) && Objects.nonNull(param.getCameraType());
    }

}
