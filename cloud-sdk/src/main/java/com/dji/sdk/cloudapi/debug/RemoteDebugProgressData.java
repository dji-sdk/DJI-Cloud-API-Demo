package com.dji.sdk.cloudapi.debug;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
public class RemoteDebugProgressData {

    private Integer percent;

    private Integer currentStep;

    private Integer totalSteps;

    private RemoteDebugStepKeyEnum stepKey;

    private Integer stepResult;

    public RemoteDebugProgressData() {
    }

    @Override
    public String toString() {
        return "RemoteDebugProgressData{" +
                "percent=" + percent +
                ", currentStep=" + currentStep +
                ", totalSteps=" + totalSteps +
                ", stepKey='" + stepKey + '\'' +
                ", stepResult=" + stepResult +
                '}';
    }

    public Integer getPercent() {
        return percent;
    }

    public RemoteDebugProgressData setPercent(Integer percent) {
        this.percent = percent;
        return this;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public RemoteDebugProgressData setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
        return this;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public RemoteDebugProgressData setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
        return this;
    }

    public RemoteDebugStepKeyEnum getStepKey() {
        return stepKey;
    }

    public RemoteDebugProgressData setStepKey(RemoteDebugStepKeyEnum stepKey) {
        this.stepKey = stepKey;
        return this;
    }

    public Integer getStepResult() {
        return stepResult;
    }

    public RemoteDebugProgressData setStepResult(Integer stepResult) {
        this.stepResult = stepResult;
        return this;
    }
}
