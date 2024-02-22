package com.dji.sdk.cloudapi.control;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class PhotoTakeProgressData {

    private PhotoTakeProgressStepEnum currentStep;

    private Integer percent;

    public PhotoTakeProgressData() {
    }

    @Override
    public String toString() {
        return "PhotoTakeProgressData{" +
                "currentStep=" + currentStep +
                ", percent=" + percent +
                '}';
    }

    public PhotoTakeProgressStepEnum getCurrentStep() {
        return currentStep;
    }

    public PhotoTakeProgressData setCurrentStep(PhotoTakeProgressStepEnum currentStep) {
        this.currentStep = currentStep;
        return this;
    }

    public Integer getPercent() {
        return percent;
    }

    public PhotoTakeProgressData setPercent(Integer percent) {
        this.percent = percent;
        return this;
    }
}
