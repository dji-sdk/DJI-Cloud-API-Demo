package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class DongleInfos {

    private List<DongleInfo> dongleInfos;

    public DongleInfos() {
    }

    @Override
    public String toString() {
        return "DongleInfos{" +
                "dongleInfos=" + dongleInfos +
                '}';
    }

    public List<DongleInfo> getDongleInfos() {
        return dongleInfos;
    }

    public DongleInfos setDongleInfos(List<DongleInfo> dongleInfos) {
        this.dongleInfos = dongleInfos;
        return this;
    }
}
