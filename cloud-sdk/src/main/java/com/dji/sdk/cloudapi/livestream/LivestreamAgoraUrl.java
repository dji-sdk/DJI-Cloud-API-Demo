package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public class LivestreamAgoraUrl extends BaseModel implements ILivestreamUrl {

    @NotNull
    private String channel;

    @NotNull
    private String sn;

    @NotNull
    private String token;

    @NotNull
    private Integer uid;

    public LivestreamAgoraUrl() {
    }

    @Override
    public String toString() {
        return "channel=" + channel +
                "&sn=" + sn +
                "&token=" + URLEncoder.encode(token, Charset.defaultCharset()) +
                "&uid=" + uid;
    }

    @Override
    public LivestreamAgoraUrl clone() {
        try {
            return (LivestreamAgoraUrl) super.clone();
        } catch (CloneNotSupportedException e) {
            return new LivestreamAgoraUrl().setSn(sn).setToken(token).setChannel(channel).setUid(uid);
        }
    }

    public String getChannel() {
        return channel;
    }

    public LivestreamAgoraUrl setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public LivestreamAgoraUrl setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LivestreamAgoraUrl setToken(String token) {
        this.token = token;
        return this;
    }

    public Integer getUid() {
        return uid;
    }

    public LivestreamAgoraUrl setUid(Integer uid) {
        this.uid = uid;
        return this;
    }
}
