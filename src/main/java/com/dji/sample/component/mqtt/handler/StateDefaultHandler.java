package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    public CommonTopicReceiver handleState(Map<String, Object> dataNode, CommonTopicReceiver stateReceiver, String sn) throws JsonProcessingException {
        // If no suitable handler is found for the data, it is not processed.
        return stateReceiver;
    }
}
