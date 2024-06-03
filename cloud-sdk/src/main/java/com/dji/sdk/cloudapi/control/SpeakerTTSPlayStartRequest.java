/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;
import jakarta.validation.constraints.NotNull;

public class SpeakerTTSPlayStartRequest extends BaseModel {

    @NotNull
    Integer psdkIndex;

    @NotNull
    SpeakerTTSPlayStartRequestTTS tts;

    public Integer getPsdkIndex() {
        return psdkIndex;
    }

    public void setPsdkIndex(Integer psdkIndex) {
        this.psdkIndex = psdkIndex;
    }

    public SpeakerTTSPlayStartRequestTTS getTts() {
        return tts;
    }

    public void setTts(SpeakerTTSPlayStartRequestTTS tts) {
        this.tts = tts;
    }
}
