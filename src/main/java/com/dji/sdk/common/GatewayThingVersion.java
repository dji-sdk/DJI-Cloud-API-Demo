package com.dji.sdk.common;

import java.util.Objects;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public class GatewayThingVersion {

    private DockThingVersionEnum dockThingVersion;

    private RcThingVersionEnum rcThingVersion;

    public GatewayThingVersion(DockThingVersionEnum dockThingVersion) {
        this.dockThingVersion = dockThingVersion;
    }

    public GatewayThingVersion(RcThingVersionEnum rcThingVersion) {
        this.rcThingVersion = rcThingVersion;
    }

    public GatewayThingVersion(GatewayTypeEnum type, String thingVersion) {
        switch (type) {
            case DOCK:
                this.dockThingVersion = DockThingVersionEnum.find(thingVersion);
                return;
            case RC:
                this.rcThingVersion = RcThingVersionEnum.find(thingVersion);
                return;
        }
    }

    public String getThingVersion() {
        if (Objects.nonNull(dockThingVersion)) {
            return dockThingVersion.getThingVersion();
        }
        return rcThingVersion.getThingVersion();
    }

    public CloudSDKVersionEnum getCloudSDKVersion() {
        if (Objects.nonNull(dockThingVersion)) {
            return dockThingVersion.getCloudSDKVersion();
        }
        return rcThingVersion.getCloudSDKVersion();
    }
}
