package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
public abstract class AbstractStateTopicHandler {

    protected AbstractStateTopicHandler handler;
    protected static ObjectMapper mapper = new ObjectMapper();;

    protected AbstractStateTopicHandler(AbstractStateTopicHandler handler){
        this.handler = handler;
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Passing dataNode data, using different processing methods depending on the data selection.
     * @param dataNode
     * @param stateReceiver
     * @param sn
     * @return
     * @throws JsonProcessingException
     */
    public abstract TopicStateReceiver handleState(JsonNode dataNode, TopicStateReceiver stateReceiver, String sn) throws JsonProcessingException;
}
