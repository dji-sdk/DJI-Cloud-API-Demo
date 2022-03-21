package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.manage.model.receiver.DeviceBasicReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
@Service
public class StateDeviceBasicHandler extends AbstractStateTopicHandler {

    public StateDeviceBasicHandler(@Autowired @Qualifier("statePayloadHandler") AbstractStateTopicHandler handler) {
        super(handler);
    }

    @Override
    public TopicStateReceiver handleState(JsonNode dataNode, TopicStateReceiver stateReceiver, String sn) throws JsonProcessingException {
        // handle device basic data
        if (dataNode.size() != 1) {
            DeviceBasicReceiver data = mapper.treeToValue(dataNode, DeviceBasicReceiver.class);
            data.setDeviceSn(sn);
            stateReceiver.setData(data);
            return stateReceiver;
        }
        return handler.handleState(dataNode, stateReceiver, sn);
    }
}
