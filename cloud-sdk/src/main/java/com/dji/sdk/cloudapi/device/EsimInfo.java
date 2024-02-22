package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class EsimInfo {

    /**
     * esim supported operators
     */
    private TelecomOperatorEnum telecomOperator;

    /**
     * In esim infos, only one esim can be enabled at the same time.
     */
    private Boolean enabled;

    /**
     * The unique identification mark of the sim card can be used to purchase physical sim card packages.
     */
    private String iccid;

    public EsimInfo() {
    }

    @Override
    public String toString() {
        return "EsimInfo{" +
                "telecomOperator=" + telecomOperator +
                ", enabled=" + enabled +
                ", iccid='" + iccid + '\'' +
                '}';
    }

    public TelecomOperatorEnum getTelecomOperator() {
        return telecomOperator;
    }

    public EsimInfo setTelecomOperator(TelecomOperatorEnum telecomOperator) {
        this.telecomOperator = telecomOperator;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public EsimInfo setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getIccid() {
        return iccid;
    }

    public EsimInfo setIccid(String iccid) {
        this.iccid = iccid;
        return this;
    }
}
