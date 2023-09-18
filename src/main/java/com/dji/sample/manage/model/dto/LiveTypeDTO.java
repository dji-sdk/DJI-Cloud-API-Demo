package com.dji.sample.manage.model.dto;

import com.dji.sdk.cloudapi.livestream.LensChangeVideoTypeEnum;
import com.dji.sdk.cloudapi.livestream.UrlTypeEnum;
import com.dji.sdk.cloudapi.livestream.VideoQualityEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Receive live parameters.
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/22
 */
@Data
public class LiveTypeDTO {

    @JsonProperty("url_type")
    private UrlTypeEnum urlType;

    private String url;

    @JsonProperty("video_id")
    private String videoId;

    @JsonProperty("video_quality")
    private VideoQualityEnum videoQuality;

    private LensChangeVideoTypeEnum videoType;

}