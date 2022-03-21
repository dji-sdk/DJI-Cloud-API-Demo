package com.dji.sample.map.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GroupDTO {

    private String id;

    private String name;

    private Integer type;
    
    private List<GroupElementDTO> elements;

    @JsonProperty(value = "is_distributed")
    private Boolean isDistributed;

    @JsonProperty(value = "is_lock")
    private Boolean isLock;

    @JsonProperty(value = "create_time")
    private Long createTime;
}
