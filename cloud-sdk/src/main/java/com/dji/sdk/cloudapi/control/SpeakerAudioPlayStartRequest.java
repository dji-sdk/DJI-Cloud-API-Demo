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

public class SpeakerAudioPlayStartRequest extends BaseModel {

    @NotNull
    Integer psdkIndex;

    @NotNull
    SpeakerAudioPlayStartRequestFile file;


    public Integer getPsdkIndex() {
        return psdkIndex;
    }

    public void setPsdkIndex(Integer psdkIndex) {
        this.psdkIndex = psdkIndex;
    }

    public SpeakerAudioPlayStartRequestFile getFile() {
        return file;
    }

    public void setFile(SpeakerAudioPlayStartRequestFile file) {
        this.file = file;
    }
}
