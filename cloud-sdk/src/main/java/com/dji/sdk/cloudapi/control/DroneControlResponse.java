package com.dji.sdk.cloudapi.control;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class DroneControlResponse {

    private Long seq;

    public DroneControlResponse() {
    }

    @Override
    public String toString() {
        return "DroneControlResponse{" +
                "seq=" + seq +
                '}';
    }

    public Long getSeq() {
        return seq;
    }

    public DroneControlResponse setSeq(Long seq) {
        this.seq = seq;
        return this;
    }

}
