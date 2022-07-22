package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
@Slf4j
public class InboundMessageRouter extends AbstractMessageRouter {

    @Resource(name = ChannelName.INBOUND)
    private MessageChannel inboundChannel;

    @Resource(name = ChannelName.INBOUND_STATUS)
    private MessageChannel statusChannel;

    @Resource(name = ChannelName.INBOUND_STATE)
    private MessageChannel stateChannel;

    @Resource(name = ChannelName.DEFAULT)
    private MessageChannel defaultChannel;

    @Resource(name = ChannelName.INBOUND_SERVICE_REPLY)
    private MessageChannel serviceReplyChannel;

    @Resource(name = ChannelName.INBOUND_OSD)
    private MessageChannel osdChannel;

    @Resource(name = ChannelName.INBOUND_REQUESTS)
    private MessageChannel requestsChannel;

    @Resource(name = ChannelName.INBOUND_EVENTS)
    private MessageChannel eventsChannel;

    private static final Pattern PATTERN_TOPIC_STATUS =
            Pattern.compile("^" + BASIC_PRE + PRODUCT + REGEX_SN + STATUS_SUF + "$");

    private static final Pattern PATTERN_TOPIC_STATE =
            Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + STATE_SUF + "$");

    private static final Pattern PATTERN_TOPIC_SERVICE_REPLY =
            Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + SERVICES_SUF + _REPLY_SUF + "$");

    private static final Pattern PATTERN_TOPIC_OSD =
            Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + OSD_SUF + "$");

    private static final Pattern PATTERN_TOPIC_REQUESTS =
            Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + REQUESTS_SUF + "$");

    private static final Pattern PATTERN_TOPIC_EVENTS =
            Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + EVENTS_SUF + "$");

    /**
     * All mqtt broker messages will arrive here before distributing them to different channels.
     * @param message message from mqtt broker
     * @return channel
     */
    @Override
    @Router(inputChannel = ChannelName.INBOUND)
    protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        byte[] payload = (byte[])message.getPayload();

        // osd
        if (PATTERN_TOPIC_OSD.matcher(topic).matches()) {
            return Collections.singleton(osdChannel);
        }

        log.debug("received topic :{} \t payload :{}", topic, new String(payload));

        // status
        if (PATTERN_TOPIC_STATUS.matcher(topic).matches()) {
            return Collections.singleton(statusChannel);
        }

        // state
        if (PATTERN_TOPIC_STATE.matcher(topic).matches()) {
            return Collections.singleton(stateChannel);
        }

        // services_reply
        if (PATTERN_TOPIC_SERVICE_REPLY.matcher(topic).matches()) {
            return Collections.singleton(serviceReplyChannel);
        }

        // requests
        if (PATTERN_TOPIC_REQUESTS.matcher(topic).matches()) {
            return Collections.singleton(requestsChannel);
        }

        // events
        if (PATTERN_TOPIC_EVENTS.matcher(topic).matches()) {
            return Collections.singleton(eventsChannel);
        }

        return Collections.singleton(defaultChannel);
    }
}
