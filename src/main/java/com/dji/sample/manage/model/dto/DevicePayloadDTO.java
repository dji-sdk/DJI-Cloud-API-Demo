package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DevicePayloadDTO {

    private String payloadSn;

    private String payloadName;

    private Integer payloadIndex;

    private String payloadDesc;
}