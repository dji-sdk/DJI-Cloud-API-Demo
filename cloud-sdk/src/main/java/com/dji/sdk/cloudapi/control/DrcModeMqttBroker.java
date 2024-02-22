package com.dji.sdk.cloudapi.control;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
public class DrcModeMqttBroker {

    @NotNull
    private String address;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String clientId;

    @NotNull
    @Min(1234567890)
    @Max(9999999999L)
    private Long expireTime;

    @NotNull
    private Boolean enableTls;

    public DrcModeMqttBroker() {
    }

    @Override
    public String toString() {
        return "DrcModeMqttBroker{" +
                "address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", clientId='" + clientId + '\'' +
                ", expireTime=" + expireTime +
                ", enableTls=" + enableTls +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public DrcModeMqttBroker setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DrcModeMqttBroker setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DrcModeMqttBroker setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public DrcModeMqttBroker setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public DrcModeMqttBroker setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public Boolean getEnableTls() {
        return enableTls;
    }

    public DrcModeMqttBroker setEnableTls(Boolean enableTls) {
        this.enableTls = enableTls;
        return this;
    }
}
