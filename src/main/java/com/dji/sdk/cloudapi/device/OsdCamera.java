package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/8
 */
public class OsdCamera {

    private CameraModeEnum cameraMode;

    private LiveviewWorldRegion liveviewWorldRegion;

    private PayloadIndex payloadIndex;

    private CameraStateEnum photoState;

    private Integer recordTime;

    private CameraStateEnum recordingState;

    private Long remainPhotoNum;

    private Integer remainRecordDuration;

    private Float zoomFactor;

    private Float irZoomFactor;

    public OsdCamera() {
    }

    @Override
    public String toString() {
        return "OsdCamera{" +
                "cameraMode=" + cameraMode +
                ", liveviewWorldRegion=" + liveviewWorldRegion +
                ", payloadIndex=" + payloadIndex +
                ", photoState=" + photoState +
                ", recordTime=" + recordTime +
                ", recordingState=" + recordingState +
                ", remainPhotoNum=" + remainPhotoNum +
                ", remainRecordDuration=" + remainRecordDuration +
                ", zoomFactor=" + zoomFactor +
                ", irZoomFactor=" + irZoomFactor +
                '}';
    }

    public CameraModeEnum getCameraMode() {
        return cameraMode;
    }

    public OsdCamera setCameraMode(CameraModeEnum cameraMode) {
        this.cameraMode = cameraMode;
        return this;
    }

    public LiveviewWorldRegion getLiveviewWorldRegion() {
        return liveviewWorldRegion;
    }

    public OsdCamera setLiveviewWorldRegion(LiveviewWorldRegion liveviewWorldRegion) {
        this.liveviewWorldRegion = liveviewWorldRegion;
        return this;
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public OsdCamera setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public CameraStateEnum getPhotoState() {
        return photoState;
    }

    public OsdCamera setPhotoState(CameraStateEnum photoState) {
        this.photoState = photoState;
        return this;
    }

    public Integer getRecordTime() {
        return recordTime;
    }

    public OsdCamera setRecordTime(Integer recordTime) {
        this.recordTime = recordTime;
        return this;
    }

    public CameraStateEnum getRecordingState() {
        return recordingState;
    }

    public OsdCamera setRecordingState(CameraStateEnum recordingState) {
        this.recordingState = recordingState;
        return this;
    }

    public Long getRemainPhotoNum() {
        return remainPhotoNum;
    }

    public OsdCamera setRemainPhotoNum(Long remainPhotoNum) {
        this.remainPhotoNum = remainPhotoNum;
        return this;
    }

    public Integer getRemainRecordDuration() {
        return remainRecordDuration;
    }

    public OsdCamera setRemainRecordDuration(Integer remainRecordDuration) {
        this.remainRecordDuration = remainRecordDuration;
        return this;
    }

    public Float getZoomFactor() {
        return zoomFactor;
    }

    public OsdCamera setZoomFactor(Float zoomFactor) {
        this.zoomFactor = zoomFactor;
        return this;
    }

    public Float getIrZoomFactor() {
        return irZoomFactor;
    }

    public OsdCamera setIrZoomFactor(Float irZoomFactor) {
        this.irZoomFactor = irZoomFactor;
        return this;
    }
}
