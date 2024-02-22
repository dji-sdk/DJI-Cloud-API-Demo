package com.dji.sdk.cloudapi.log;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
public class LogFileProgress {

    private Integer currentStep;

    private Integer totalStep;

    private Integer progress;

    private Long finishTime;

    private Float uploadRate;

    private FileUploadStatusEnum status;

    private Integer result;

    public LogFileProgress() {
    }

    @Override
    public String toString() {
        return "LogFileProgress{" +
                "currentStep=" + currentStep +
                ", totalStep=" + totalStep +
                ", progress=" + progress +
                ", finishTime=" + finishTime +
                ", uploadRate=" + uploadRate +
                ", status=" + status +
                ", result=" + result +
                '}';
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public LogFileProgress setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
        return this;
    }

    public Integer getTotalStep() {
        return totalStep;
    }

    public LogFileProgress setTotalStep(Integer totalStep) {
        this.totalStep = totalStep;
        return this;
    }

    public Integer getProgress() {
        return progress;
    }

    public LogFileProgress setProgress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public LogFileProgress setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public Float getUploadRate() {
        return uploadRate;
    }

    public LogFileProgress setUploadRate(Float uploadRate) {
        this.uploadRate = uploadRate;
        return this;
    }

    public FileUploadStatusEnum getStatus() {
        return status;
    }

    public LogFileProgress setStatus(FileUploadStatusEnum status) {
        this.status = status;
        return this;
    }

    public Integer getResult() {
        return result;
    }

    public LogFileProgress setResult(Integer result) {
        this.result = result;
        return this;
    }
}
