package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public class LivestreamRtspUrl extends BaseModel implements ILivestreamUrl {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Integer port;

    public LivestreamRtspUrl() {
    }

    @Override
    public String toString() {
        return "userName=" + username +
                "&password=" + password +
                "&port=" + port;
    }

    @Override
    public LivestreamRtspUrl clone() {
        try {
            return (LivestreamRtspUrl) super.clone();
        } catch (CloneNotSupportedException e) {
            return new LivestreamRtspUrl().setUsername(username).setPassword(password).setPort(port);
        }
    }

    public String getUsername() {
        return username;
    }

    public LivestreamRtspUrl setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LivestreamRtspUrl setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public LivestreamRtspUrl setPort(Integer port) {
        this.port = port;
        return this;
    }
}
