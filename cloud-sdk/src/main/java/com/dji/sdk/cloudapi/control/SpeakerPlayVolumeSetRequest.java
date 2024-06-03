/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SpeakerPlayVolumeSetRequest {

    @NotNull
    Integer psdkIndex;

    @NotNull
    @Min(0)
    @Max(100)
    Integer playVolume;
}
