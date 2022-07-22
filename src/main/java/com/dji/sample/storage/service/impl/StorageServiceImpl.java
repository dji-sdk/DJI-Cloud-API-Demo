package com.dji.sample.storage.service.impl;

import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.media.model.StsCredentialsDTO;
import com.dji.sample.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/3/9
 */
@Service
public class StorageServiceImpl implements IStorageService {

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private OssServiceContext ossService;

    @Autowired
    private OssConfiguration configuration;

    @Override
    public StsCredentialsDTO getSTSCredentials() {
        return StsCredentialsDTO.builder()
                .endpoint(configuration.getEndpoint())
                .bucket(configuration.getBucket())
                .credentials(ossService.getCredentials())
                .provider(configuration.getProvider())
                .objectKeyPrefix(configuration.getObjectDirPrefix())
                .region(configuration.getRegion())
                .build();
    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_STORAGE_CONFIG_GET, outputChannel = ChannelName.OUTBOUND)
    public void replyConfigGet(CommonTopicReceiver receiver, MessageHeaders headers) {
        CommonTopicResponse<RequestsReply> response = CommonTopicResponse.<RequestsReply>builder()
                .tid(receiver.getTid())
                .bid(receiver.getBid())
                .data(RequestsReply.success(this.getSTSCredentials()))
                .timestamp(System.currentTimeMillis())
                .method(receiver.getMethod())
                .build();
        messageSender.publish(headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF, response);
    }
}
