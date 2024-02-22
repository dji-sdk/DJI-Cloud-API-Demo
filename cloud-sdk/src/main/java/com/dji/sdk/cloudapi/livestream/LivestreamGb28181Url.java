package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public class LivestreamGb28181Url extends BaseModel implements ILivestreamUrl {

    @NotNull
    private String serverIP;

    @NotNull
    private Integer serverPort;

    @NotNull
    private String serverID;

    @NotNull
    private String agentID;

    @NotNull
    private String agentPassword;

    @NotNull
    private Integer localPort;

    @NotNull
    private String channel;

    public LivestreamGb28181Url() {
    }

    @Override
    public String toString() {
        return "serverIP=" + serverIP +
                "&serverPort=" + serverPort +
                "&serverID=" + serverID +
                "&agentID=" + agentID +
                "&agentPassword=" + agentPassword +
                "&localPort=" + localPort +
                "&channel=" + channel;
    }

    @Override
    public LivestreamGb28181Url clone() {
        try {
            return (LivestreamGb28181Url) super.clone();
        } catch (CloneNotSupportedException e) {
            return new LivestreamGb28181Url()
                    .setServerIP(serverIP)
                    .setServerPort(serverPort)
                    .setServerID(serverID)
                    .setAgentID(agentID)
                    .setAgentPassword(agentPassword)
                    .setLocalPort(localPort)
                    .setChannel(channel);
        }
    }

    public String getServerIP() {
        return serverIP;
    }

    public LivestreamGb28181Url setServerIP(String serverIP) {
        this.serverIP = serverIP;
        return this;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public LivestreamGb28181Url setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public String getServerID() {
        return serverID;
    }

    public LivestreamGb28181Url setServerID(String serverID) {
        this.serverID = serverID;
        return this;
    }

    public String getAgentID() {
        return agentID;
    }

    public LivestreamGb28181Url setAgentID(String agentID) {
        this.agentID = agentID;
        return this;
    }

    public String getAgentPassword() {
        return agentPassword;
    }

    public LivestreamGb28181Url setAgentPassword(String agentPassword) {
        this.agentPassword = agentPassword;
        return this;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public LivestreamGb28181Url setLocalPort(Integer localPort) {
        this.localPort = localPort;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public LivestreamGb28181Url setChannel(String channel) {
        this.channel = channel;
        return this;
    }
}
