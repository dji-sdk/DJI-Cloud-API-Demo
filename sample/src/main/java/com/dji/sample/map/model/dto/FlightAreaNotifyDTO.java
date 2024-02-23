package com.dji.sample.map.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightAreaNotifyDTO {

    private String sn;

    private Integer result;

    private String status;

    private String message;
}
