package com.dji.sdk.cloudapi.firmware;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/28
 */
public class OtaCreateRequest extends BaseModel {

    @Size(min = 1, max = 2)
    @NotNull
    private List<@Valid OtaCreateDevice> devices;

    public OtaCreateRequest() {
    }

    @Override
    public String toString() {
        return "OtaCreateRequest{" +
                "devices=" + devices +
                '}';
    }

    public List<OtaCreateDevice> getDevices() {
        return devices;
    }

    public OtaCreateRequest setDevices(List<OtaCreateDevice> devices) {
        this.devices = devices;
        return this;
    }
}
