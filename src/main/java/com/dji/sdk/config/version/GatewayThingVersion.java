package com.dji.sdk.config.version;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public class GatewayThingVersion {

    private IThingVersion thingVersion;

    public GatewayThingVersion(IThingVersion thingVersion) {
        this.thingVersion = thingVersion;
    }

    public GatewayThingVersion(GatewayTypeEnum type, String thingVersion) {
        switch (type) {
            case DOCK:
                this.thingVersion = DockThingVersionEnum.find(thingVersion);
                return;
            case DOCK2:
                this.thingVersion = Dock2ThingVersionEnum.find(thingVersion);
                return;
            case RC:
                this.thingVersion = RcThingVersionEnum.find(thingVersion);
                return;
        }
    }

    public String getThingVersion() {
        return thingVersion.getThingVersion();
    }

    public CloudSDKVersionEnum getCloudSDKVersion() {
        return thingVersion.getCloudSDKVersion();
    }
}
