package com.dji.sample.map.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElementResourceDTO {

    private Integer type;

    @JsonProperty(value = "user_name")
    private String username;

    private ResourceContentDTO content;
}
