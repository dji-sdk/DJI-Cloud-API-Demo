package com.dji.sdk.cloudapi.device;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public class RcPayloadControlSource {

    private ControlSourceEnum controlSource;

    private PayloadIndex payloadIndex;

    private String sn;

    private String firmwareVersion;

    public RcPayloadControlSource() {
    }

    @Override
    public String toString() {
        return "RcPayloadControlSource{" +
                "controlSource=" + controlSource +
                ", payloadIndex=" + payloadIndex +
                ", sn='" + sn + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }

    public ControlSourceEnum getControlSource() {
        return controlSource;
    }

    public RcPayloadControlSource setControlSource(ControlSourceEnum controlSource) {
        this.controlSource = controlSource;
        return this;
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public RcPayloadControlSource setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public RcPayloadControlSource setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public RcPayloadControlSource setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }
}