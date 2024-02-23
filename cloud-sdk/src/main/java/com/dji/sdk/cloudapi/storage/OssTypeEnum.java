package com.dji.sdk.cloudapi.storage;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/30
 */
@Schema(description = "oss type", example = "minio", enumAsRef = true)
public enum OssTypeEnum {

    ALIYUN("ali"),

    AWS("aws"),

    MINIO("minio");

    private String type;

    OssTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
