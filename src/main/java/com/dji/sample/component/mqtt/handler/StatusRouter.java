package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/12
 * @version 0.1
 */
@MessageEndpoint
public class StatusRouter {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Converts the status data sent by the gateway device into an object.
     * @param message
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS, outputChannel = ChannelName.INBOUND_STATUS_ROUTER)
    public CommonTopicReceiver<StatusGatewayReceiver> resolveStatus(Message<?> message) {
        CommonTopicReceiver<StatusGatewayReceiver> statusReceiver = new CommonTopicReceiver<>();
        try {
            statusReceiver = mapper.readValue(
                    (byte[])message.getPayload(),
                    new TypeReference<CommonTopicReceiver<StatusGatewayReceiver>>() {});

            String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();

            // set gateway's sn
            statusReceiver.getData().setSn(
                    topic.substring((BASIC_PRE + PRODUCT).length(),
                            topic.indexOf(STATUS_SUF)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusReceiver;
    }

    /**
     * Handles the routing of status topic messages. Depending on the data, it is assigned to different channels for handling.
     * @param receiver
     * @return
     */
    @Router(inputChannel = ChannelName.INBOUND_STATUS_ROUTER)
    public String resolveStatusRouter(CommonTopicReceiver<StatusGatewayReceiver> receiver) {
        // Determine whether the drone is online or offline according to whether the data of the sub-device is empty.
        return CollectionUtils.isEmpty(receiver.getData().getSubDevices()) ?
                ChannelName.INBOUND_STATUS_OFFLINE : ChannelName.INBOUND_STATUS_ONLINE;
    }

}