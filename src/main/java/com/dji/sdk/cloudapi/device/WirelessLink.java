package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
public class WirelessLink {

    @JsonProperty("4g_freq_band")
    private Float fourthGenerationFreqBand;

    @JsonProperty("4g_gnd_quality")
    private Integer fourthGenerationGndQuality;

    @JsonProperty("4g_link_state")
    private Boolean fourthGenerationLinkState;

    @JsonProperty("4g_quality")
    private Integer fourthGenerationQuality;

    @JsonProperty("4g_uav_quality")
    private Integer fourthGenerationUavQuality;

    private Integer dongleNumber;

    private LinkWorkModeEnum linkWorkmode;

    private Float sdrFreqBand;

    private Boolean sdrLinkState;

    private Integer sdrQuality;

    public WirelessLink() {
    }

    @Override
    public String toString() {
        return "WirelessLink{" +
                "fourthGenerationFreqBand=" + fourthGenerationFreqBand +
                ", fourthGenerationGndQuality=" + fourthGenerationGndQuality +
                ", fourthGenerationLinkState=" + fourthGenerationLinkState +
                ", fourthGenerationQuality=" + fourthGenerationQuality +
                ", fourthGenerationUavQuality=" + fourthGenerationUavQuality +
                ", dongleNumber=" + dongleNumber +
                ", linkWorkmode=" + linkWorkmode +
                ", sdrFreqBand=" + sdrFreqBand +
                ", sdrLinkState=" + sdrLinkState +
                ", sdrQuality=" + sdrQuality +
                '}';
    }

    public Float getFourthGenerationFreqBand() {
        return fourthGenerationFreqBand;
    }

    public WirelessLink setFourthGenerationFreqBand(Float fourthGenerationFreqBand) {
        this.fourthGenerationFreqBand = fourthGenerationFreqBand;
        return this;
    }

    public Integer getFourthGenerationGndQuality() {
        return fourthGenerationGndQuality;
    }

    public WirelessLink setFourthGenerationGndQuality(Integer fourthGenerationGndQuality) {
        this.fourthGenerationGndQuality = fourthGenerationGndQuality;
        return this;
    }

    public Boolean getFourthGenerationLinkState() {
        return fourthGenerationLinkState;
    }

    public WirelessLink setFourthGenerationLinkState(Boolean fourthGenerationLinkState) {
        this.fourthGenerationLinkState = fourthGenerationLinkState;
        return this;
    }

    public Integer getFourthGenerationQuality() {
        return fourthGenerationQuality;
    }

    public WirelessLink setFourthGenerationQuality(Integer fourthGenerationQuality) {
        this.fourthGenerationQuality = fourthGenerationQuality;
        return this;
    }

    public Integer getFourthGenerationUavQuality() {
        return fourthGenerationUavQuality;
    }

    public WirelessLink setFourthGenerationUavQuality(Integer fourthGenerationUavQuality) {
        this.fourthGenerationUavQuality = fourthGenerationUavQuality;
        return this;
    }

    public Integer getDongleNumber() {
        return dongleNumber;
    }

    public WirelessLink setDongleNumber(Integer dongleNumber) {
        this.dongleNumber = dongleNumber;
        return this;
    }

    public LinkWorkModeEnum getLinkWorkmode() {
        return linkWorkmode;
    }

    public WirelessLink setLinkWorkmode(LinkWorkModeEnum linkWorkmode) {
        this.linkWorkmode = linkWorkmode;
        return this;
    }

    public Float getSdrFreqBand() {
        return sdrFreqBand;
    }

    public WirelessLink setSdrFreqBand(Float sdrFreqBand) {
        this.sdrFreqBand = sdrFreqBand;
        return this;
    }

    public Boolean getSdrLinkState() {
        return sdrLinkState;
    }

    public WirelessLink setSdrLinkState(Boolean sdrLinkState) {
        this.sdrLinkState = sdrLinkState;
        return this;
    }

    public Integer getSdrQuality() {
        return sdrQuality;
    }

    public WirelessLink setSdrQuality(Integer sdrQuality) {
        this.sdrQuality = sdrQuality;
        return this;
    }
}
