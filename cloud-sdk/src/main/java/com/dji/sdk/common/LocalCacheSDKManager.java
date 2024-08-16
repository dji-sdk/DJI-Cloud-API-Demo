/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月25日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.dji.sdk.cloudapi.device.DeviceSubTypeEnum;
import com.dji.sdk.cloudapi.device.DeviceTypeEnum;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheSDKManager implements SDKManager{

    final ConcurrentHashMap<String, GatewayManager> SDK_MAP = new ConcurrentHashMap<>(16);


    @Override
    public GatewayManager getDeviceSDK(String gatewaySn) {
        if (SDK_MAP.containsKey(gatewaySn)) {
            return SDK_MAP.get(gatewaySn);
        }
        throw new CloudSDKException(CloudSDKErrorEnum.NOT_REGISTERED,
                "The device has not been registered, please call the 'SDKManager.registerDevice()' method to register the device first.");
    }

    @Override
    public Optional<GatewayManager> findDeviceSDK(String gatewaySn) {
        if(SDK_MAP.containsKey(gatewaySn)){
            return Optional.of(SDK_MAP.get(gatewaySn));
        }else {
            return Optional.empty();
        }
    }

    @Override
    public GatewayManager registerDevice(String gatewaySn, String droneSn, DeviceDomainEnum domain, DeviceTypeEnum type, DeviceSubTypeEnum subType, String gatewayThingVersion, String droneThingVersion) {
        return registerDevice(gatewaySn, droneSn, GatewayTypeEnum.find(DeviceEnum.find(domain, type, subType)), gatewayThingVersion, droneThingVersion);
    }

    @Override
    public GatewayManager registerDevice(String gatewaySn, String droneSn, GatewayTypeEnum type, String gatewayThingVersion, String droneThingVersion) {
        return registerDevice(new GatewayManager(Objects.requireNonNull(gatewaySn), droneSn, type, gatewayThingVersion, droneThingVersion));
    }

    @Override
    public GatewayManager registerDevice(GatewayManager gateway) {
        SDK_MAP.put(gateway.getGatewaySn(), gateway);
        return gateway;
    }

    @Override
    public void logoutDevice(String gatewaySn) {
        SDK_MAP.remove(gatewaySn);
    }
}
