package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class SimInfo {

    /**
     * sim supported operators
     */
    private TelecomOperatorEnum telecomOperator;

    /**
     * Type of physical sim card
     */
    private SimTypeEnum simType;

    /**
     * The unique identification mark of the sim card can be used to purchase physical sim card packages.
     */
    private String iccid;

    public SimInfo() {
    }

    @Override
    public String toString() {
        return "SimInfo{" +
                "telecomOperator=" + telecomOperator +
                ", simType=" + simType +
                ", iccid='" + iccid + '\'' +
                '}';
    }

    public TelecomOperatorEnum getTelecomOperator() {
        return telecomOperator;
    }

    public SimInfo setTelecomOperator(TelecomOperatorEnum telecomOperator) {
        this.telecomOperator = telecomOperator;
        return this;
    }

    public SimTypeEnum getSimType() {
        return simType;
    }

    public SimInfo setSimType(SimTypeEnum simType) {
        this.simType = simType;
        return this;
    }

    public String getIccid() {
        return iccid;
    }

    public SimInfo setIccid(String iccid) {
        this.iccid = iccid;
        return this;
    }
}
