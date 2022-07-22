package com.dji.sample.media.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileExtensionDTO {

    private String droneModelKey;

    private Boolean isOriginal;

    private String payloadModelKey;

    private String tinnyFingerprint;

    private String sn;

    private String flightId;

}
