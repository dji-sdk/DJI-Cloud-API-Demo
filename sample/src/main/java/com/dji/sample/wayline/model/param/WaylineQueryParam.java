package com.dji.sample.wayline.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaylineQueryParam {

    private boolean favorited;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int pageSize = 10;

    private String orderBy;

    private Integer[] templateType;
}
