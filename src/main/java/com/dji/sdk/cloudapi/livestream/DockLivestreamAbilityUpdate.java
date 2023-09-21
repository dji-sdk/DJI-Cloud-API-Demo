package com.dji.sdk.cloudapi.livestream;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class DockLivestreamAbilityUpdate {

    private DockLiveCapacity liveCapacity;

    public DockLivestreamAbilityUpdate() {
    }

    @Override
    public String toString() {
        return "DockLivestreamAbilityUpdate{" +
                "liveCapacity=" + liveCapacity +
                '}';
    }

    public DockLiveCapacity getLiveCapacity() {
        return liveCapacity;
    }

    public DockLivestreamAbilityUpdate setLiveCapacity(DockLiveCapacity liveCapacity) {
        this.liveCapacity = liveCapacity;
        return this;
    }
}
