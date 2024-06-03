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

public class SpeakerPlayModeSetRequest extends BaseModel {

    //"0":"单次播放","1":"循环播放(单曲)"
    @NotNull
    Integer playMode;

    @NotNull
    Integer psdkIndex;

    public Integer getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Integer playMode) {
        this.playMode = playMode;
    }

    public Integer getPsdkIndex() {
        return psdkIndex;
    }

    public void setPsdkIndex(Integer psdkIndex) {
        this.psdkIndex = psdkIndex;
    }
}
