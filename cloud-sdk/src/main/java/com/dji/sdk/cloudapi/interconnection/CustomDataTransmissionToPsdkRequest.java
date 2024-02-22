package com.dji.sdk.cloudapi.interconnection;

import com.dji.sdk.common.BaseModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class CustomDataTransmissionToPsdkRequest extends BaseModel {

    /**
     * Data content
     * length: Less than 256
     */
    @NotNull
    @Length(max = 256)
    private String value;

    public CustomDataTransmissionToPsdkRequest() {
    }

    @Override
    public String toString() {
        return "CustomDataTransmissionToPsdkRequest{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public CustomDataTransmissionToPsdkRequest setValue(String value) {
        this.value = value;
        return this;
    }
}
