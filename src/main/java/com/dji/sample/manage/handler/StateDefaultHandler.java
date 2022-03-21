package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/3/21
 */
@Service
public class StateDefaultHandler extends AbstractStateTopicHandler {

    protected StateDefaultHandler() {
        super(null);
    }

    @Override
    public TopicStateReceiver handleState(JsonNode dataNode, TopicStateReceiver stateReceiver, String sn) throws JsonProcessingException {
        // If no suitable handler is found for the data, it is not processed.
        return stateReceiver;
    }
}
