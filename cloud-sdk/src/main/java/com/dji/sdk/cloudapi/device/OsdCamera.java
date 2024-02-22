package com.dji.sdk.cloudapi.device;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.control.*;
import com.dji.sdk.config.version.CloudSDKVersionEnum;

import java.util.List;

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

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private Boolean screenSplitEnable;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private List<LensStorageSettingsEnum> photoStorageSettings;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private List<LensStorageSettingsEnum> videoStorageSettings;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private ExposureModeEnum wideExposureMode;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private CameraIsoEnum wideIso;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private ShutterSpeedEnum wideShutterSpeed;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private ExposureValueEnum wideExposureValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private ExposureModeEnum zoomExposureMode;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private CameraIsoEnum zoomIso;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private ShutterSpeedEnum zoomShutterSpeed;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private ExposureValueEnum zoomExposureValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private FocusModeEnum zoomFocusMode;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private Integer zoomFocusValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private Integer zoomMaxFocusValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private Integer zoomMinFocusValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private MeteringModeEnum irMeteringMode;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private IrMeteringPoint irMeteringPoint;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private IrMeteringArea irMeteringArea;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private Integer zoomCalibrateFarthestFocusValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private Integer zoomCalibrateNearestFocusValue;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2)
    private FocusStateEnum zoomFocusState;

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
                ", screenSplitEnable=" + screenSplitEnable +
                ", photoStorageSettings=" + photoStorageSettings +
                ", videoStorageSettings=" + videoStorageSettings +
                ", wideExposureMode=" + wideExposureMode +
                ", wideIso=" + wideIso +
                ", wideShutterSpeed=" + wideShutterSpeed +
                ", wideExposureValue=" + wideExposureValue +
                ", zoomExposureMode=" + zoomExposureMode +
                ", zoomIso=" + zoomIso +
                ", zoomShutterSpeed=" + zoomShutterSpeed +
                ", zoomExposureValue=" + zoomExposureValue +
                ", zoomFocusMode=" + zoomFocusMode +
                ", zoomFocusValue=" + zoomFocusValue +
                ", zoomMaxFocusValue=" + zoomMaxFocusValue +
                ", zoomMinFocusValue=" + zoomMinFocusValue +
                ", irMeteringMode=" + irMeteringMode +
                ", irMeteringPoint=" + irMeteringPoint +
                ", irMeteringArea=" + irMeteringArea +
                ", zoomCalibrateFarthestFocusValue=" + zoomCalibrateFarthestFocusValue +
                ", zoomCalibrateNearestFocusValue=" + zoomCalibrateNearestFocusValue +
                ", zoomFocusState=" + zoomFocusState +
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

    public Boolean getScreenSplitEnable() {
        return screenSplitEnable;
    }

    public OsdCamera setScreenSplitEnable(Boolean screenSplitEnable) {
        this.screenSplitEnable = screenSplitEnable;
        return this;
    }

    public List<LensStorageSettingsEnum> getPhotoStorageSettings() {
        return photoStorageSettings;
    }

    public OsdCamera setPhotoStorageSettings(List<LensStorageSettingsEnum> photoStorageSettings) {
        this.photoStorageSettings = photoStorageSettings;
        return this;
    }

    public List<LensStorageSettingsEnum> getVideoStorageSettings() {
        return videoStorageSettings;
    }

    public OsdCamera setVideoStorageSettings(List<LensStorageSettingsEnum> videoStorageSettings) {
        this.videoStorageSettings = videoStorageSettings;
        return this;
    }

    public ExposureModeEnum getWideExposureMode() {
        return wideExposureMode;
    }

    public OsdCamera setWideExposureMode(ExposureModeEnum wideExposureMode) {
        this.wideExposureMode = wideExposureMode;
        return this;
    }

    public CameraIsoEnum getWideIso() {
        return wideIso;
    }

    public OsdCamera setWideIso(CameraIsoEnum wideIso) {
        this.wideIso = wideIso;
        return this;
    }

    public ShutterSpeedEnum getWideShutterSpeed() {
        return wideShutterSpeed;
    }

    public OsdCamera setWideShutterSpeed(ShutterSpeedEnum wideShutterSpeed) {
        this.wideShutterSpeed = wideShutterSpeed;
        return this;
    }

    public ExposureValueEnum getWideExposureValue() {
        return wideExposureValue;
    }

    public OsdCamera setWideExposureValue(ExposureValueEnum wideExposureValue) {
        this.wideExposureValue = wideExposureValue;
        return this;
    }

    public ExposureModeEnum getZoomExposureMode() {
        return zoomExposureMode;
    }

    public OsdCamera setZoomExposureMode(ExposureModeEnum zoomExposureMode) {
        this.zoomExposureMode = zoomExposureMode;
        return this;
    }

    public CameraIsoEnum getZoomIso() {
        return zoomIso;
    }

    public OsdCamera setZoomIso(CameraIsoEnum zoomIso) {
        this.zoomIso = zoomIso;
        return this;
    }

    public ShutterSpeedEnum getZoomShutterSpeed() {
        return zoomShutterSpeed;
    }

    public OsdCamera setZoomShutterSpeed(ShutterSpeedEnum zoomShutterSpeed) {
        this.zoomShutterSpeed = zoomShutterSpeed;
        return this;
    }

    public ExposureValueEnum getZoomExposureValue() {
        return zoomExposureValue;
    }

    public OsdCamera setZoomExposureValue(ExposureValueEnum zoomExposureValue) {
        this.zoomExposureValue = zoomExposureValue;
        return this;
    }

    public FocusModeEnum getZoomFocusMode() {
        return zoomFocusMode;
    }

    public OsdCamera setZoomFocusMode(FocusModeEnum zoomFocusMode) {
        this.zoomFocusMode = zoomFocusMode;
        return this;
    }

    public Integer getZoomFocusValue() {
        return zoomFocusValue;
    }

    public OsdCamera setZoomFocusValue(Integer zoomFocusValue) {
        this.zoomFocusValue = zoomFocusValue;
        return this;
    }

    public Integer getZoomMaxFocusValue() {
        return zoomMaxFocusValue;
    }

    public OsdCamera setZoomMaxFocusValue(Integer zoomMaxFocusValue) {
        this.zoomMaxFocusValue = zoomMaxFocusValue;
        return this;
    }

    public Integer getZoomMinFocusValue() {
        return zoomMinFocusValue;
    }

    public OsdCamera setZoomMinFocusValue(Integer zoomMinFocusValue) {
        this.zoomMinFocusValue = zoomMinFocusValue;
        return this;
    }

    public MeteringModeEnum getIrMeteringMode() {
        return irMeteringMode;
    }

    public OsdCamera setIrMeteringMode(MeteringModeEnum irMeteringMode) {
        this.irMeteringMode = irMeteringMode;
        return this;
    }

    public IrMeteringPoint getIrMeteringPoint() {
        return irMeteringPoint;
    }

    public OsdCamera setIrMeteringPoint(IrMeteringPoint irMeteringPoint) {
        this.irMeteringPoint = irMeteringPoint;
        return this;
    }

    public IrMeteringArea getIrMeteringArea() {
        return irMeteringArea;
    }

    public OsdCamera setIrMeteringArea(IrMeteringArea irMeteringArea) {
        this.irMeteringArea = irMeteringArea;
        return this;
    }

    public Integer getZoomCalibrateFarthestFocusValue() {
        return zoomCalibrateFarthestFocusValue;
    }

    public OsdCamera setZoomCalibrateFarthestFocusValue(Integer zoomCalibrateFarthestFocusValue) {
        this.zoomCalibrateFarthestFocusValue = zoomCalibrateFarthestFocusValue;
        return this;
    }

    public Integer getZoomCalibrateNearestFocusValue() {
        return zoomCalibrateNearestFocusValue;
    }

    public OsdCamera setZoomCalibrateNearestFocusValue(Integer zoomCalibrateNearestFocusValue) {
        this.zoomCalibrateNearestFocusValue = zoomCalibrateNearestFocusValue;
        return this;
    }

    public FocusStateEnum getZoomFocusState() {
        return zoomFocusState;
    }

    public OsdCamera setZoomFocusState(FocusStateEnum zoomFocusState) {
        this.zoomFocusState = zoomFocusState;
        return this;
    }
}
