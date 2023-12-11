package com.dji.sdk.config.version;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public enum RcThingVersionEnum implements IThingVersion {

    V1_0_0("1.0.0", CloudSDKVersionEnum.V0_0_1);

    private final String thingVersion;

    private final CloudSDKVersionEnum cloudSDKVersion;

    RcThingVersionEnum(String thingVersion, CloudSDKVersionEnum cloudSDKVersion) {
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

    public static RcThingVersionEnum find(String thingVersion) {
        return V1_0_0;
    }
}
