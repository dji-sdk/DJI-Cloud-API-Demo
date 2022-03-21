package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.3
 * @date 2022/1/5
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IconUrlDTO {

    @JsonProperty("normal_icon_url")
    private String normalUrl;

    @JsonProperty("selected_icon_url")
    private String selectUrl;
}
