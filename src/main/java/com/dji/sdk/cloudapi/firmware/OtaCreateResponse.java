package com.dji.sdk.cloudapi.firmware;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class OtaCreateResponse {

    /**
     * Mission status
     **/
    private OtaProgressStatusEnum status;

    public OtaCreateResponse() {
    }

    @Override
    public String toString() {
        return "OtaCreateResponse{" +
                "status=" + status +
                '}';
    }

    public OtaProgressStatusEnum getStatus() {
        return status;
    }

    public OtaCreateResponse setStatus(OtaProgressStatusEnum status) {
        this.status = status;
        return this;
    }
}
