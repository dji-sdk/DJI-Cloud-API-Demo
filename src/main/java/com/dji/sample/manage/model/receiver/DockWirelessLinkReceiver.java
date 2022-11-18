package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
@Data
public class DockWirelessLinkReceiver {

    @JsonProperty("4g_freq_band")
    private Float fourGFreqBand;

    @JsonProperty("4g_gnd_quality")
    private Integer fourGGndQuality;

    @JsonProperty("4g_link_state")
    private Integer fourGLinkState;

    @JsonProperty("4g_quality")
    private Integer fourGQuality;

    @JsonProperty("4g_uav_quality")
    private Integer fourGUavQuality;

    private Integer dongleNumber;

    private Integer linkWorkmode;

    private Float sdrFreqBand;

    private Integer sdrLinkState;

    private Integer sdrQuality;
}
