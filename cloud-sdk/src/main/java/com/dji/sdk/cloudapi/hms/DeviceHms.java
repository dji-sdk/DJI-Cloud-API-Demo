package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.cloudapi.device.DeviceEnum;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
public class DeviceHms {

    private String code;

    private DeviceEnum deviceType;

    private Boolean imminent;

    private Boolean inTheSky;

    private HmsLevelEnum level;

    private HmsModuleEnum module;

    private DeviceHmsArgs args;

    public DeviceHms() {
    }

    @Override
    public String toString() {
        return "DeviceHms{" +
                "code='" + code + '\'' +
                ", deviceType=" + deviceType +
                ", imminent=" + imminent +
                ", inTheSky=" + inTheSky +
                ", level=" + level +
                ", module=" + module +
                ", args=" + args +
                '}';
    }

    public String getCode() {
        return code;
    }

    public DeviceHms setCode(String code) {
        this.code = code;
        return this;
    }

    public DeviceEnum getDeviceType() {
        return deviceType;
    }

    public DeviceHms setDeviceType(DeviceEnum deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public Boolean getImminent() {
        return imminent;
    }

    public DeviceHms setImminent(Boolean imminent) {
        this.imminent = imminent;
        return this;
    }

    public Boolean getInTheSky() {
        return inTheSky;
    }

    public DeviceHms setInTheSky(Boolean inTheSky) {
        this.inTheSky = inTheSky;
        return this;
    }

    public HmsLevelEnum getLevel() {
        return level;
    }

    public DeviceHms setLevel(HmsLevelEnum level) {
        this.level = level;
        return this;
    }

    public HmsModuleEnum getModule() {
        return module;
    }

    public DeviceHms setModule(HmsModuleEnum module) {
        this.module = module;
        return this;
    }

    public DeviceHmsArgs getArgs() {
        return args;
    }

    public DeviceHms setArgs(DeviceHmsArgs args) {
        this.args = args;
        return this;
    }
}
