package com.dji.sample.media.model;

import lombok.Data;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Data
public class FileUploadDTO {

    private FileExtensionDTO ext;

    private String fingerprint;

    private String name;

    private String path;

    private FileMetadataDTO metadata;

    private String objectKey;

    private Integer subFileType;
}
