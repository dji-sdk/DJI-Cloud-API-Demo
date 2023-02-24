package com.dji.sample.wayline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaylineTaskFileDTO {

    private String url;

    private String fingerprint;
}
