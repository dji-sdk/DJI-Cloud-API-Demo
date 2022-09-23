package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.manage.model.receiver.LogsFileUploadList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Component
public class ServicesReplyHandler {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Handle the reply message from the pilot side to the on-demand video.
     * @param message   reply message
     * @throws IOException
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_SERVICE_REPLY)
    public void serviceReply(Message<?> message) throws IOException {
        byte[] payload = (byte[])message.getPayload();

        CommonTopicReceiver receiver = mapper.readValue(payload, new TypeReference<CommonTopicReceiver>() {});
        if (ServicesMethodEnum.FILE_UPLOAD_LIST.getMethod().equals(receiver.getMethod())) {
            LogsFileUploadList list = mapper.convertValue(receiver.getData(), new TypeReference<LogsFileUploadList>() {});
            receiver.setData(list);
        } else {
            ServiceReply reply = mapper.convertValue(receiver.getData(), new TypeReference<ServiceReply>() {});
            receiver.setData(reply);
        }
        Chan<CommonTopicReceiver<?>> chan = Chan.getInstance();
        // Put the message to the chan object.
        chan.put(receiver);
    }
}
