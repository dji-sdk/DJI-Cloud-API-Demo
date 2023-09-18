package com.dji.sdk.cloudapi.firmware;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
public class OtaProgress {

    private OtaProgressStatusEnum status;

    private OtaProgressData progress;

    private OtaProgressExt ext;

    public OtaProgress() {
    }

    @Override
    public String toString() {
        return "OtaProgress{" +
                "status=" + status +
                ", progress=" + progress +
                ", ext=" + ext +
                '}';
    }

    public OtaProgressStatusEnum getStatus() {
        return status;
    }

    public OtaProgress setStatus(OtaProgressStatusEnum status) {
        this.status = status;
        return this;
    }

    public OtaProgressData getProgress() {
        return progress;
    }

    public OtaProgress setProgress(OtaProgressData progress) {
        this.progress = progress;
        return this;
    }

    public OtaProgressExt getExt() {
        return ext;
    }

    public OtaProgress setExt(OtaProgressExt ext) {
        this.ext = ext;
        return this;
    }
}
