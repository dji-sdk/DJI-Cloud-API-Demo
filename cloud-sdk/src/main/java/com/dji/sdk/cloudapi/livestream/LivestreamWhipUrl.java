package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public class LivestreamWhipUrl extends BaseModel implements ILivestreamUrl {

    @NotNull
    private String url;

    public LivestreamWhipUrl() {
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public LivestreamWhipUrl clone() {
        try {
            return (LivestreamWhipUrl) super.clone();
        } catch (CloneNotSupportedException e) {
            return new LivestreamWhipUrl().setUrl(url);
        }
    }

    public String getUrl() {
        return url;
    }

    public LivestreamWhipUrl setUrl(String url) {
        this.url = url;
        return this;
    }
}
