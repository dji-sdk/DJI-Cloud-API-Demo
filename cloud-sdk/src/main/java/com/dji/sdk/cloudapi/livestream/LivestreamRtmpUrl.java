package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public class LivestreamRtmpUrl extends BaseModel implements ILivestreamUrl {

    @NotNull
    private String url;

    public LivestreamRtmpUrl() {
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public LivestreamRtmpUrl clone() {
        try {
            return (LivestreamRtmpUrl) super.clone();
        } catch (CloneNotSupportedException e) {
            return new LivestreamRtmpUrl().setUrl(url);
        }
    }

    public LivestreamRtmpUrl setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrl() {
        return url;
    }
}
