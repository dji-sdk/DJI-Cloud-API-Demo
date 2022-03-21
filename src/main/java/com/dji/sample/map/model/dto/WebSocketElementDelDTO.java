package com.dji.sample.map.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/2
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketElementDelDTO {

    @JsonProperty("id")
    private String elementId;

    @JsonProperty("group_id")
    private String groupId;
}
