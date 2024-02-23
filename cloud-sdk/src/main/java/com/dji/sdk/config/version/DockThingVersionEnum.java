package com.dji.sdk.config.version;

import com.dji.sdk.exception.CloudSDKVersionException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public enum DockThingVersionEnum implements IThingVersion {

    V1_0_0("1.0.0", CloudSDKVersionEnum.V0_0_1),

    V1_1_0("1.1.0", CloudSDKVersionEnum.V0_0_1),

    V1_1_2("1.1.2", CloudSDKVersionEnum.V1_0_0),

    V1_1_3("1.1.3", CloudSDKVersionEnum.V1_0_2),

    ;

    private final String thingVersion;

    private final CloudSDKVersionEnum cloudSDKVersion;

    DockThingVersionEnum(String thingVersion, CloudSDKVersionEnum cloudSDKVersion) {
        this.thingVersion = thingVersion;
        this.cloudSDKVersion = cloudSDKVersion;
    }

    @JsonValue
    public String getThingVersion() {
        return thingVersion;
    }

    public CloudSDKVersionEnum getCloudSDKVersion() {
        return cloudSDKVersion;
    }

    public static DockThingVersionEnum find(String thingVersion) {
        return Arrays.stream(values()).filter(thingVersionEnum -> thingVersionEnum.thingVersion.equals(thingVersion))
                .findAny().orElseThrow(() -> new CloudSDKVersionException(thingVersion));
    }
}
