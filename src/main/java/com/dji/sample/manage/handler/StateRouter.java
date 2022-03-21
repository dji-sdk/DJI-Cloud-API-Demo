package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.manage.model.receiver.CapacityDeviceReceiver;
import com.dji.sample.manage.model.receiver.DeviceBasicReceiver;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.MessageRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@MessageEndpoint
@Slf4j
@Configuration
public class StateRouter {

    @Resource(name = "stateDeviceBasicHandler")
    private AbstractStateTopicHandler handler;

    /**
     * Handles the routing of state topic messages. Depending on the data, it is assigned to different channels for handling.
     * @param message
     * @return
     * @throws IOException
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE, outputChannel = ChannelName.INBOUND_STATE_SPLITTER)
    public TopicStateReceiver<?> resolveStateData(Message<?> message) throws IOException {
        byte[] payload = (byte[])message.getPayload();
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TopicStateReceiver stateReceiver = mapper.readValue(payload, TopicStateReceiver.class);
        // Get the sn of the topic source.
        String from = topic.substring((THING_MODEL_PRE + PRODUCT).length(),
                topic.indexOf(STATE_SUF));

        try {
            JsonNode dataNode = mapper.readTree(payload).findPath("data");
            return handler.handleState(dataNode, stateReceiver, from);

        } catch (UnrecognizedPropertyException e) {
            log.info("The {} data is not processed.", e.getPropertyName());
        }
        return stateReceiver;
    }

    @Bean
    @Router(inputChannel = ChannelName.INBOUND_STATE_ROUTER)
    public MessageRouter resolveStateRouter() {
        PayloadTypeRouter router = new PayloadTypeRouter();
        // // Channel mapping for basic data.
        router.setChannelMapping(DeviceBasicReceiver.class.getName(),
                ChannelName.INBOUND_STATE_BASIC);
        // Channel mapping for live streaming capabilities.
        router.setChannelMapping(CapacityDeviceReceiver.class.getName(),
                ChannelName.INBOUND_STATE_CAPACITY);
        // Channel mapping for payload data.
        router.setChannelMapping(List.class.getName(),
                ChannelName.INBOUND_STATE_PAYLOAD);
        router.setChannelMapping(Map.class.getName(),
                ChannelName.DEFAULT);
        return router;
    }

}