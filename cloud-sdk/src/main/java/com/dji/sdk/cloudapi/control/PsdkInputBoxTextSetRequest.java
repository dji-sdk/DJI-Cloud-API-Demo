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

public class PsdkInputBoxTextSetRequest extends BaseModel {

    @NotNull
    Integer psdkIndex;

    String value;
}
