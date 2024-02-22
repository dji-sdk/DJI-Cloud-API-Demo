package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/19
 */
public class DongleInfo {

    /**
     * Dongle’s unique identification mark
     */
    private String imei;

    /**
     * dongle type
     */
    private DongleTypeEnum dongleType;

    /**
     * The unique identification mark of eSIM is used for public account query packages and purchase services.
     */
    private String eid;

    /**
     * esim activation status
     */
    private EsimActivateStateEnum esimActivateState;

    /**
     * The status of the physical sim card in the dongle.
     */
    private SimCardStateEnum simCardState;

    /**
     * Identifies the sim card slot currently being used by the dongle.
     */
    private SimSlotEnum simSlot;

    /**
     * esim information
     */
    private List<EsimInfo> esimInfos;

    /**
     * Physical sim card information that can be inserted into the dongle.
     */
    private SimInfo simInfo;

    public DongleInfo() {
    }

    @Override
    public String toString() {
        return "DongleInfo{" +
                "imei='" + imei + '\'' +
                ", dongleType=" + dongleType +
                ", eid='" + eid + '\'' +
                ", esimActivateState=" + esimActivateState +
                ", simCardState=" + simCardState +
                ", simSlot=" + simSlot +
                ", esimInfos=" + esimInfos +
                ", simInfo=" + simInfo +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public DongleInfo setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public DongleTypeEnum getDongleType() {
        return dongleType;
    }

    public DongleInfo setDongleType(DongleTypeEnum dongleType) {
        this.dongleType = dongleType;
        return this;
    }

    public String getEid() {
        return eid;
    }

    public DongleInfo setEid(String eid) {
        this.eid = eid;
        return this;
    }

    public EsimActivateStateEnum getEsimActivateState() {
        return esimActivateState;
    }

    public DongleInfo setEsimActivateState(EsimActivateStateEnum esimActivateState) {
        this.esimActivateState = esimActivateState;
        return this;
    }

    public SimCardStateEnum getSimCardState() {
        return simCardState;
    }

    public DongleInfo setSimCardState(SimCardStateEnum simCardState) {
        this.simCardState = simCardState;
        return this;
    }

    public SimSlotEnum getSimSlot() {
        return simSlot;
    }

    public DongleInfo setSimSlot(SimSlotEnum simSlot) {
        this.simSlot = simSlot;
        return this;
    }

    public List<EsimInfo> getEsimInfos() {
        return esimInfos;
    }

    public DongleInfo setEsimInfos(List<EsimInfo> esimInfos) {
        this.esimInfos = esimInfos;
        return this;
    }

    public SimInfo getSimInfo() {
        return simInfo;
    }

    public DongleInfo setSimInfo(SimInfo simInfo) {
        this.simInfo = simInfo;
        return this;
    }
}
