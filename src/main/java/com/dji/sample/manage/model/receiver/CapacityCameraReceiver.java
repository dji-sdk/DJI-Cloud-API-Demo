package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CapacityCameraReceiver {

    private Integer availableVideoNumber;

    private Integer coexistVideoNumberMax;

    private String cameraIndex;

    @JsonProperty(value = "video_list")
    private List<CapacityVideoReceiver> videosList;

}