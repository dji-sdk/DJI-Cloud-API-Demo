package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.cloudapi.device.LinkWorkModeEnum;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public class SdrWorkmodeSwitchRequest extends BaseModel {

    @NotNull
    private LinkWorkModeEnum linkWorkmode;

    public SdrWorkmodeSwitchRequest() {
    }

    @Override
    public String toString() {
        return "SdrWorkmodeSwitchRequest{" +
                "linkWorkmode=" + linkWorkmode +
                '}';
    }

    public LinkWorkModeEnum getLinkWorkmode() {
        return linkWorkmode;
    }

    public SdrWorkmodeSwitchRequest setLinkWorkmode(LinkWorkModeEnum linkWorkmode) {
        this.linkWorkmode = linkWorkmode;
        return this;
    }
}
