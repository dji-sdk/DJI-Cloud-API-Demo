package com.dji.sample.media.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileMetadataDTO {

    private Double absoluteAltitude;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    private Date createdTime;

    private Double gimbalYawDegree;

    private PositionDTO photoedPosition;

    private PositionDTO shootPosition;

    private Double relativeAltitude;
}
