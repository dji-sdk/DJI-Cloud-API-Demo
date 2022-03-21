package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LiveStatusReceiver {

    private Long liveTime;

    private Integer liveTrendline;

    private String videoId;

    private Integer videoQuality;
}