package com.dji.sdk.cloudapi.log;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public enum LogModuleEnum {
    
    DRONE("0"),

    DOCK ("3");

    private final String domain;

    LogModuleEnum(String domain) {
        this.domain = domain;
    }

    @JsonCreator
    public static LogModuleEnum find(String domain) {
        return Arrays.stream(values()).filter(domainEnum -> domainEnum.domain.equals(domain)).findAny()
                .orElseThrow(() -> new CloudSDKException(LogModuleEnum.class, domain));
    }

    @JsonValue
    public String getDomain() {
        return domain;
    }
}
