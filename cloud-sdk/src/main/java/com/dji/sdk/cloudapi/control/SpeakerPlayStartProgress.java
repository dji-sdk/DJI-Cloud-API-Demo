/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

public class SpeakerPlayStartProgress {

    String md5;
    Integer psdkIndex;
    String status;
    SpeakerPlayProgress progress;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getPsdkIndex() {
        return psdkIndex;
    }

    public void setPsdkIndex(Integer psdkIndex) {
        this.psdkIndex = psdkIndex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SpeakerPlayProgress getProgress() {
        return progress;
    }

    public void setProgress(SpeakerPlayProgress progress) {
        this.progress = progress;
    }
}
