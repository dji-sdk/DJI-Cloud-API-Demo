package com.dji.sdk.cloudapi.interconnection;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class CustomDataTransmissionFromEsdk {

    /**
     * Data content
     * length: Less than 256
     */
    private String value;

    public CustomDataTransmissionFromEsdk() {
    }

    @Override
    public String toString() {
        return "CustomDataTransmissionFromEsdk{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public CustomDataTransmissionFromEsdk setValue(String value) {
        this.value = value;
        return this;
    }
}
