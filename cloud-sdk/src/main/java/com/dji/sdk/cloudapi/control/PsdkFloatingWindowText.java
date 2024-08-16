/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

public class PsdkFloatingWindowText {
    String value;
    Integer psdkIndex;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getPsdkIndex() {
        return psdkIndex;
    }

    public void setPsdkIndex(Integer psdkIndex) {
        this.psdkIndex = psdkIndex;
    }
}
