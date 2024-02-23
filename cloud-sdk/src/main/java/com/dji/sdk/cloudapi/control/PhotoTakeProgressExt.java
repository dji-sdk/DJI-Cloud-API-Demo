package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.CameraModeEnum;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class PhotoTakeProgressExt {

    private CameraModeEnum cameraMode;

    public PhotoTakeProgressExt() {
    }

    @Override
    public String toString() {
        return "PhotoTakeProgressExt{" +
                "cameraMode=" + cameraMode +
                '}';
    }

    public CameraModeEnum getCameraMode() {
        return cameraMode;
    }

    public PhotoTakeProgressExt setCameraMode(CameraModeEnum cameraMode) {
        this.cameraMode = cameraMode;
        return this;
    }
}
