package com.dji.sample.map.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightAreaFileDTO {

    private String fileId;

    private String workspaceId;

    private String name;

    private String objectKey;

    private String sign;

    private Integer size;

    private Boolean latest;
}
