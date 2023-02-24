package com.dji.sample.wayline.model.dto;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Data
public class WaylineTaskProgressReceiver {

    private WaylineTaskProgressExt ext;

    private WaylineTaskProgress progress;

    private String status;

}
