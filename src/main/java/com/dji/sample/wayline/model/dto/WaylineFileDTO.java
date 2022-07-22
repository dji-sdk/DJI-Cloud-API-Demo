package com.dji.sample.wayline.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaylineFileDTO {

    private String name;

    @JsonProperty("id")
    private String waylineId;

    private String droneModelKey;

    private String sign;

    private List<String> payloadModelKeys;

    private Boolean favorited;

    private List<Integer> templateTypes;

    private String objectKey;

    @JsonProperty("user_name")
    private String username;

    private Long updateTime;
}
