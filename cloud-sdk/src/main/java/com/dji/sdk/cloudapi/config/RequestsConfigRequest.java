package com.dji.sdk.cloudapi.config;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
public class RequestsConfigRequest {

    private ConfigTypeEnum configType;

    private ConfigScopeEnum configScope;

    public RequestsConfigRequest() {
    }

    @Override
    public String toString() {
        return "RequestsConfigRequest{" +
                "configType=" + configType +
                ", configScope=" + configScope +
                '}';
    }

    public ConfigTypeEnum getConfigType() {
        return configType;
    }

    public RequestsConfigRequest setConfigType(ConfigTypeEnum configType) {
        this.configType = configType;
        return this;
    }

    public ConfigScopeEnum getConfigScope() {
        return configScope;
    }

    public RequestsConfigRequest setConfigScope(ConfigScopeEnum configScope) {
        this.configScope = configScope;
        return this;
    }
}
