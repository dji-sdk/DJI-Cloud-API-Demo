package com.dji.sample.component.oss.model.enums;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/30
 */
public enum OssTypeEnum {

    ALIYUN("ali"),

    AWS("aws"),

    MINIO("minio");

    private String type;

    OssTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
