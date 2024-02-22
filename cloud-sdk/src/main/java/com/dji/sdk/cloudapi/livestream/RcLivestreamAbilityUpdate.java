package com.dji.sdk.cloudapi.livestream;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class RcLivestreamAbilityUpdate {

    private RcLiveCapacity liveCapacity;

    public RcLivestreamAbilityUpdate() {
    }

    @Override
    public String toString() {
        return "RcLivestreamAbilityUpdate{" +
                "liveCapacity=" + liveCapacity +
                '}';
    }

    public RcLiveCapacity getLiveCapacity() {
        return liveCapacity;
    }

    public RcLivestreamAbilityUpdate setLiveCapacity(RcLiveCapacity liveCapacity) {
        this.liveCapacity = liveCapacity;
        return this;
    }
}
