/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

public class SpeakerPlayProgress {
    Integer percent;
    String stepKey;

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getStepKey() {
        return stepKey;
    }

    public void setStepKey(String stepKey) {
        this.stepKey = stepKey;
    }
}
