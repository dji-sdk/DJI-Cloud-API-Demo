package com.dji.sample.map.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElementCoordinateDTO {

    private Double longitude;

    private Double latitude;

    private Double altitude;

}
