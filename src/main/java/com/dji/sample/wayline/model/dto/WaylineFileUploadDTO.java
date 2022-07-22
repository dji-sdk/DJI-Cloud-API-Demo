package com.dji.sample.wayline.model.dto;

import lombok.Data;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
@Data
public class WaylineFileUploadDTO {

    private String objectKey;

    private String name;

    private WaylineFileDTO metadata;
}
