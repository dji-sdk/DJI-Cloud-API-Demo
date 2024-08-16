/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月04日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;
import jakarta.validation.constraints.NotNull;

public class PsdkWidgetValueSetRequest extends BaseModel {

    @NotNull
    Integer index;

    @NotNull
    Integer psdkIndex;
    Integer value;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPsdkIndex() {
        return psdkIndex;
    }

    public void setPsdkIndex(Integer psdkIndex) {
        this.psdkIndex = psdkIndex;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
