package com.dji.sample.manage.model.dto;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sdk.cloudapi.livestream.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */

@Data
@ConfigurationProperties("livestream.url")
@Configuration
public class LiveStreamProperty {

    private static LivestreamAgoraUrl agora;

    private static LivestreamRtmpUrl rtmp;

    private static LivestreamRtspUrl rtsp;

    private static LivestreamGb28181Url gb28181;

    private static LivestreamWhipUrl whip;

    public void setAgora(LivestreamAgoraUrl agora) {
        LiveStreamProperty.agora = agora;
    }

    public void setRtmp(LivestreamRtmpUrl rtmp) {
        LiveStreamProperty.rtmp = rtmp;
    }

    public void setRtsp(LivestreamRtspUrl rtsp) {
        LiveStreamProperty.rtsp = rtsp;
    }

    public void setGb28181(LivestreamGb28181Url gb28181) {
        LiveStreamProperty.gb28181 = gb28181;
    }

    public void setWhip(LivestreamWhipUrl webrtc) {
        LiveStreamProperty.whip = webrtc;
    }

    public static ILivestreamUrl get(UrlTypeEnum type) {
        switch (type) {
            case AGORA:
                return agora;
            case RTMP:
                return rtmp;
            case RTSP:
                return rtsp;
            case GB28181:
                return gb28181;
            case WHIP:
                return whip;
        }
        throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
    }
}
