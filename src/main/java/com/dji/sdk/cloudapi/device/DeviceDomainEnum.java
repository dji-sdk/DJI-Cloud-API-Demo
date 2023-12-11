package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 *
 * @author sean.zhou
 * @date 2021/11/15
 * @version 0.1
 */
@Schema(description = "device domain", enumAsRef = true)
public enum DeviceDomainEnum {

    DRONE(0),

    PAYLOAD(1),

    REMOTER_CONTROL(2),

    DOCK (3),

    ;

    private final int domain;

    DeviceDomainEnum(int domain) {
        this.domain = domain;
    }

    @JsonCreator
    public static DeviceDomainEnum find(int domain) {
        return Arrays.stream(values()).filter(domainEnum -> domainEnum.domain == domain).findAny()
                .orElseThrow(() -> new CloudSDKException(DeviceDomainEnum.class, domain));
    }

    @JsonValue
    public int getDomain() {
        return domain;
    }
}
