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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceContentDTO {

    @Builder.Default
    private String type = "Feature";

    private ContentPropertyDTO properties;

    private ElementType geometry;
}
