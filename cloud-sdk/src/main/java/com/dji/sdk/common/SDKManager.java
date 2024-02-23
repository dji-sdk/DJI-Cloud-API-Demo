package com.dji.sdk.common;

import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.dji.sdk.cloudapi.device.DeviceSubTypeEnum;
import com.dji.sdk.cloudapi.device.DeviceTypeEnum;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 *
 * fix: 改接口,由客户决定Gateway管理策略 witcom@2023.09.25
 */
public interface SDKManager {

    default GatewayManager getDeviceSDK(String gatewaySn){
        return findDeviceSDK(gatewaySn)
                .orElseThrow(()-> new CloudSDKException(CloudSDKErrorEnum.NOT_REGISTERED,
                        "The device has not been registered, please call the 'SDKManager.registerDevice()' method to register the device first."));
    }

    Optional<GatewayManager> findDeviceSDK(String gatewaySn);
    default GatewayManager registerDevice(String gatewaySn, String droneSn,
                                          DeviceDomainEnum domain, DeviceTypeEnum type, DeviceSubTypeEnum subType, String gatewayThingVersion, String droneThingVersion){
        return registerDevice(gatewaySn, droneSn, GatewayTypeEnum.find(DeviceEnum.find(domain, type, subType)), gatewayThingVersion, droneThingVersion);
    }

    default GatewayManager registerDevice(String gatewaySn, String droneSn, GatewayTypeEnum type, String gatewayThingVersion, String droneThingVersion){
        return registerDevice(new GatewayManager(Objects.requireNonNull(gatewaySn), droneSn, type, gatewayThingVersion, droneThingVersion));
    }

    GatewayManager registerDevice(GatewayManager gateway);

    void logoutDevice(String gatewaySn);

/*
    private SDKManager() {
    }

    private static final ConcurrentHashMap<String, GatewayManager> SDK_MAP = new ConcurrentHashMap<>(16);

    public static GatewayManager getDeviceSDK(String gatewaySn) {
        if (SDK_MAP.containsKey(gatewaySn)) {
            return SDK_MAP.get(gatewaySn);
        }
        throw new CloudSDKException(CloudSDKErrorEnum.NOT_REGISTERED,
                "The device has not been registered, please call the 'SDKManager.registerDevice()' method to register the device first.");
    }

    public static Optional<GatewayManager> findDeviceSDK(String gatewaySn) {
        if(SDK_MAP.containsKey(gatewaySn)){
            return Optional.of(SDK_MAP.get(gatewaySn));
        }else {
            return Optional.empty();
        }
    }

    public static GatewayManager registerDevice(String gatewaySn, String droneSn,
            DeviceDomainEnum domain, DeviceTypeEnum type, DeviceSubTypeEnum subType, String gatewayThingVersion, String droneThingVersion) {
        return registerDevice(gatewaySn, droneSn, GatewayTypeEnum.find(DeviceEnum.find(domain, type, subType)), gatewayThingVersion, droneThingVersion);
    }

    public static GatewayManager registerDevice(String gatewaySn, String droneSn, GatewayTypeEnum type, String gatewayThingVersion, String droneThingVersion) {
        return registerDevice(new GatewayManager(Objects.requireNonNull(gatewaySn), droneSn, type, gatewayThingVersion, droneThingVersion));
    }

    public static GatewayManager registerDevice(GatewayManager gateway) {
        SDK_MAP.put(gateway.getGatewaySn(), gateway);
        return gateway;
    }

    public static void logoutDevice(String gatewaySn) {
        SDK_MAP.remove(gatewaySn);
    }

 */
}
