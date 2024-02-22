package com.dji.sdk.cloudapi.debug;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
public class RemoteDebugProgress {

    private RemoteDebugStatusEnum status;

    private RemoteDebugProgressData progress;

    public RemoteDebugProgress() {
    }

    @Override
    public String toString() {
        return "RemoteDebugProgress{" +
                "status=" + status +
                ", progress=" + progress +
                '}';
    }

    public RemoteDebugStatusEnum getStatus() {
        return status;
    }

    public RemoteDebugProgress setStatus(RemoteDebugStatusEnum status) {
        this.status = status;
        return this;
    }

    public RemoteDebugProgressData getProgress() {
        return progress;
    }

    public RemoteDebugProgress setProgress(RemoteDebugProgressData progress) {
        this.progress = progress;
        return this;
    }
}
