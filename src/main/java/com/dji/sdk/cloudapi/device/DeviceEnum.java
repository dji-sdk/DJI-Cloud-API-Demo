package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
@Schema(description = "device model key.", format = "domain-type-subType", enumAsRef = true, example = "0-89-0")
public enum DeviceEnum {

    M350(DeviceDomainEnum.DRONE, DeviceTypeEnum.M350, DeviceSubTypeEnum.ZERO),

    M300(DeviceDomainEnum.DRONE, DeviceTypeEnum.M300, DeviceSubTypeEnum.ZERO),

    M30(DeviceDomainEnum.DRONE, DeviceTypeEnum.M30_OR_M3T_CAMERA, DeviceSubTypeEnum.ZERO),

    M30T(DeviceDomainEnum.DRONE, DeviceTypeEnum.M30_OR_M3T_CAMERA, DeviceSubTypeEnum.ONE),

    M3E(DeviceDomainEnum.DRONE, DeviceTypeEnum.M3E, DeviceSubTypeEnum.ZERO),

    M3T(DeviceDomainEnum.DRONE, DeviceTypeEnum.M3E, DeviceSubTypeEnum.ONE),

    M3M(DeviceDomainEnum.DRONE, DeviceTypeEnum.M3E, DeviceSubTypeEnum.TWO),

    Z30(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.Z30, DeviceSubTypeEnum.ZERO),

    XT2(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.XT2, DeviceSubTypeEnum.ZERO),

    FPV(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.FPV, DeviceSubTypeEnum.ZERO),

    XTS(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.XTS, DeviceSubTypeEnum.ZERO),

    H20(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.H20, DeviceSubTypeEnum.ZERO),

    H20T(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.H20T, DeviceSubTypeEnum.ZERO),

    P1(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.P1, DeviceSubTypeEnum._65535),

    M30_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M30_CAMERA, DeviceSubTypeEnum.ZERO),

    M30T_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M30T_CAMERA, DeviceSubTypeEnum.ZERO),

    H20N(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.H20N, DeviceSubTypeEnum.ZERO),

    DOCK_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.DOCK_CAMERA, DeviceSubTypeEnum.ZERO),

    L1(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.L1, DeviceSubTypeEnum.ZERO),

    M3E_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M3E_CAMERA, DeviceSubTypeEnum.ZERO),

    M3T_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M30_OR_M3T_CAMERA, DeviceSubTypeEnum.ZERO),

    M3M_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M3M_CAMERA, DeviceSubTypeEnum.ZERO),

    RC(DeviceDomainEnum.REMOTER_CONTROL, DeviceTypeEnum.RC, DeviceSubTypeEnum.ZERO),

    RC_PLUS(DeviceDomainEnum.REMOTER_CONTROL, DeviceTypeEnum.RC_PLUS, DeviceSubTypeEnum.ZERO),

    RC_PRO(DeviceDomainEnum.REMOTER_CONTROL, DeviceTypeEnum.RC_PRO, DeviceSubTypeEnum.ZERO),

    DOCK(DeviceDomainEnum.DOCK, DeviceTypeEnum.DOCK, DeviceSubTypeEnum.ZERO),

    DOCK2(DeviceDomainEnum.DOCK, DeviceTypeEnum.DOCK2, DeviceSubTypeEnum.ZERO),

    M3D(DeviceDomainEnum.DRONE, DeviceTypeEnum.M3D, DeviceSubTypeEnum.ZERO),

    M3TD(DeviceDomainEnum.DRONE, DeviceTypeEnum.M3D, DeviceSubTypeEnum.ONE),

    M3D_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M3D_CAMERA, DeviceSubTypeEnum.ZERO),

    M3TD_CAMERA(DeviceDomainEnum.PAYLOAD, DeviceTypeEnum.M3TD_CAMERA, DeviceSubTypeEnum.ZERO),
    ;

    @Schema(enumAsRef = true)
    private final DeviceDomainEnum domain;

    @Schema(enumAsRef = true)
    private final DeviceTypeEnum type;

    @Schema(enumAsRef = true)
    private final DeviceSubTypeEnum subType;

    DeviceEnum(DeviceDomainEnum domain, DeviceTypeEnum type, DeviceSubTypeEnum subType) {
        this.domain = domain;
        this.type = type;
        this.subType = subType;
    }

    public DeviceDomainEnum getDomain() {
        return domain;
    }

    public DeviceTypeEnum getType() {
        return type;
    }

    public DeviceSubTypeEnum getSubType() {
        return subType;
    }

    @JsonValue
    public String getDevice() {
        return String.format("%s-%s-%s", domain.getDomain(), type.getType(), subType.getSubType());
    }

    public static DeviceEnum find(DeviceDomainEnum domain, DeviceTypeEnum type, DeviceSubTypeEnum subType) {
        return DeviceEnum.find(domain.getDomain(), type.getType(), subType.getSubType());
    }

    public static DeviceEnum find(int domain, int type, int subType) {
        return Arrays.stream(values()).filter(device -> device.domain.getDomain() == domain &&
                device.type.getType() == type && device.subType.getSubType() == subType)
                .findAny().orElseThrow(() -> new CloudSDKException(DeviceEnum.class,
                        String.format("%s-%s-%s", domain, type, subType)));
    }

    @JsonCreator
    public static DeviceEnum find(String key) {
        return Arrays.stream(values()).filter(device -> device.getDevice().equals(key))
                .findAny().orElseThrow(() -> new CloudSDKException(DeviceEnum.class, key));
    }
}
