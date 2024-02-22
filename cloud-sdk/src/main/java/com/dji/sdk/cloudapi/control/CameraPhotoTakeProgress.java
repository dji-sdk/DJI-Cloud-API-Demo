package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.wayline.FlighttaskStatusEnum;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class CameraPhotoTakeProgress {

    private PhotoTakeProgressExt ext;

    private PhotoTakeProgressData progress;

    private FlighttaskStatusEnum status;

    public CameraPhotoTakeProgress() {
    }

    @Override
    public String toString() {
        return "CameraPhotoTakeProgress{" +
                "ext=" + ext +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }

    public PhotoTakeProgressExt getExt() {
        return ext;
    }

    public CameraPhotoTakeProgress setExt(PhotoTakeProgressExt ext) {
        this.ext = ext;
        return this;
    }

    public PhotoTakeProgressData getProgress() {
        return progress;
    }

    public CameraPhotoTakeProgress setProgress(PhotoTakeProgressData progress) {
        this.progress = progress;
        return this;
    }

    public FlighttaskStatusEnum getStatus() {
        return status;
    }

    public CameraPhotoTakeProgress setStatus(FlighttaskStatusEnum status) {
        this.status = status;
        return this;
    }
}
