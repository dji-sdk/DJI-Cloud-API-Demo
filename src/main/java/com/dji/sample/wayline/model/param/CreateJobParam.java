package com.dji.sample.wayline.model.param;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
public class CreateJobParam {

    private String name;

    private String fileId;

    private String dockSn;

    private String type;

    private boolean immediate;
}
