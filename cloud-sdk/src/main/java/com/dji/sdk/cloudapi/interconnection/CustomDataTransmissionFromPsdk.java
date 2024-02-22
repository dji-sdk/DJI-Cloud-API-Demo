package com.dji.sdk.cloudapi.interconnection;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class CustomDataTransmissionFromPsdk {

    /**
     * Data content
     * length: Less than 256
     */
    private String value;

    public CustomDataTransmissionFromPsdk() {
    }

    @Override
    public String toString() {
        return "CustomDataTransmissionFromPsdk{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public CustomDataTransmissionFromPsdk setValue(String value) {
        this.value = value;
        return this;
    }
}
