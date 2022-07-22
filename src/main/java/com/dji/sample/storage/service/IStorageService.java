package com.dji.sample.storage.service;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.media.model.StsCredentialsDTO;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/29
 */
public interface IStorageService {

    /**
     * Get custom temporary credentials object for uploading the media and wayline.
     * @return temporary credentials object
     */
    StsCredentialsDTO getSTSCredentials();

    /**
     * Handles requests from the dock to obtain temporary credentials.
     * @param receiver
     * @param headers
     */
    void replyConfigGet(CommonTopicReceiver receiver, MessageHeaders headers);
}
