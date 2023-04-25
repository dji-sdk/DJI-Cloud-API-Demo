package com.dji.sample.control.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/14
 */
@Data
public class PointDTO {

    @Range(min = -90, max = 90)
    @NotNull
    private Double latitude;

    @NotNull
    @Range(min = -180, max = 180)
    private Double longitude;

    /**
     * WGS84
     * The M30 series are ellipsoidal heights.
     */
    @NotNull
    @Range(min = 2, max = 1500)
    private Double height;
}
