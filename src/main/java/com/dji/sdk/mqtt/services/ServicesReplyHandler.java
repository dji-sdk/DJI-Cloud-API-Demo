package com.dji.sdk.mqtt.services;

import com.dji.sdk.cloudapi.log.FileUploadListResponse;
import com.dji.sdk.cloudapi.log.LogMethodEnum;
import com.dji.sdk.common.Common;
import com.dji.sdk.common.PublishBarrier;
import com.dji.sdk.mqtt.Chan;
import com.dji.sdk.mqtt.ChannelName;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Component
public class ServicesReplyHandler {

    @Resource
    PublishBarrier barrier;

    /**
     * Handle the reply message from topic "/services_reply".
     * @param message   reply message
     * @throws IOException
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_SERVICES_REPLY)
    public void servicesReply(Message<?> message) throws IOException {
        byte[] payload = (byte[])message.getPayload();

        TopicServicesResponse<ServicesReplyReceiver> receiver = Common.getObjectMapper()
                .readValue(payload, new TypeReference<TopicServicesResponse<ServicesReplyReceiver>>() {});
        //fix: use Barrier instead witcom@2023.09.22
//        Chan chan = Chan.getInstance(receiver.getTid(), false);
//        if (Objects.isNull(chan)) {
//            return;
//        }
        String identity = barrier.generateIdentity(receiver);
        if(!barrier.hasIdentity(identity)){
            return;
        }
        if (LogMethodEnum.FILE_UPLOAD_LIST.getMethod().equals(receiver.getMethod())) {
            receiver.getData().setOutput(Common.getObjectMapper().convertValue(receiver.getData(),
                    new TypeReference<FileUploadListResponse>() {}));
        }
        barrier.put(identity, receiver);
        //chan.put(receiver);
    }
}
