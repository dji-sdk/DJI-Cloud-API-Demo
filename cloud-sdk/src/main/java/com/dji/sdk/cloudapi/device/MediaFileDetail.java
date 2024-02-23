package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/17
 */
public class MediaFileDetail {

    private Integer remainUpload;

    public MediaFileDetail() {
    }

    @Override
    public String toString() {
        return "MediaFileDetail{" +
                "remainUpload=" + remainUpload +
                '}';
    }

    public Integer getRemainUpload() {
        return remainUpload;
    }

    public MediaFileDetail setRemainUpload(Integer remainUpload) {
        this.remainUpload = remainUpload;
        return this;
    }
}
