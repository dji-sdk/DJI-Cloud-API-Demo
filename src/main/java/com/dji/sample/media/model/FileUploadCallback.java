package com.dji.sample.media.model;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Data
public class FileUploadCallback {

    private Integer result;

    private Integer progress;

    private FileUploadDTO file;
}
