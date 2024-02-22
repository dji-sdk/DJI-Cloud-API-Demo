package com.dji.sdk.cloudapi.debug;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class RemoteDebugResponse {

    private RemoteDebugStatusEnum status;

    public RemoteDebugResponse() {
    }

    @Override
    public String toString() {
        return "RemoteDebugResponse{" +
                "status=" + status +
                '}';
    }

    public RemoteDebugStatusEnum getStatus() {
        return status;
    }

    public RemoteDebugResponse setStatus(RemoteDebugStatusEnum status) {
        this.status = status;
        return this;
    }
}
