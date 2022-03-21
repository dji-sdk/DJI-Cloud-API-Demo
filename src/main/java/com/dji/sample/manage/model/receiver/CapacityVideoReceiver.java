package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CapacityVideoReceiver {

    private String videoIndex;

    private String videoType;
}