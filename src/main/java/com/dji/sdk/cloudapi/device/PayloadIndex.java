package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class PayloadIndex {

    @NotNull
    private DeviceTypeEnum type;

    @NotNull
    private DeviceSubTypeEnum subType;

    @NotNull
    private PayloadPositionEnum position;

    public PayloadIndex() {
    }

    @JsonCreator
    public PayloadIndex(String payloadIndex) {
        Objects.requireNonNull(payloadIndex);
        int[] payloadIndexArr = Arrays.stream(payloadIndex.split("-")).mapToInt(Integer::parseInt).toArray();
        if (payloadIndexArr.length != 3) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
        this.type = DeviceTypeEnum.find(payloadIndexArr[0]);
        this.subType = DeviceSubTypeEnum.find(payloadIndexArr[1]);
        this.position = PayloadPositionEnum.find(payloadIndexArr[2]);
    }

    @Override
    @JsonValue
    public String toString() {
        return String.format("%s-%s-%s", type.getType(), subType.getSubType(), position.getPosition());
    }

    public DeviceTypeEnum getType() {
        return type;
    }

    public PayloadIndex setType(DeviceTypeEnum type) {
        this.type = type;
        return this;
    }

    public DeviceSubTypeEnum getSubType() {
        return subType;
    }

    public PayloadIndex setSubType(DeviceSubTypeEnum subType) {
        this.subType = subType;
        return this;
    }

    public PayloadPositionEnum getPosition() {
        return position;
    }

    public PayloadIndex setPosition(PayloadPositionEnum position) {
        this.position = position;
        return this;
    }
}
