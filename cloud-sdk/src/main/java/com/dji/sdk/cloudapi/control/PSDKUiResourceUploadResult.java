/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

public class PSDKUiResourceUploadResult {

    String objectKey;

    Integer psdk_index;

    Integer result;
    Integer size;


    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public Integer getPsdk_index() {
        return psdk_index;
    }

    public void setPsdk_index(Integer psdk_index) {
        this.psdk_index = psdk_index;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
