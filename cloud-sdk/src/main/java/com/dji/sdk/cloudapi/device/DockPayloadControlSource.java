package com.dji.sdk.cloudapi.device;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public class DockPayloadControlSource {

    private ControlSourceEnum controlSource;

    private PayloadIndex payloadIndex;

    private String sn;

    public DockPayloadControlSource() {
    }

    @Override
    public String toString() {
        return "RcPayloadControlSource{" +
                "controlSource=" + controlSource +
                ", payloadIndex=" + payloadIndex +
                ", sn='" + sn + '\'' +
                '}';
    }

    public ControlSourceEnum getControlSource() {
        return controlSource;
    }

    public DockPayloadControlSource setControlSource(ControlSourceEnum controlSource) {
        this.controlSource = controlSource;
        return this;
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public DockPayloadControlSource setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public DockPayloadControlSource setSn(String sn) {
        this.sn = sn;
        return this;
    }

}