package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.manage.model.enums.StateDataEnum;
import com.dji.sample.manage.model.receiver.DevicePayloadReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
@Service
public class StatePayloadHandler extends AbstractStateTopicHandler {

    protected StatePayloadHandler(@Autowired @Qualifier("stateLiveCapacityHandler") AbstractStateTopicHandler handler) {
        super(handler);
    }

    @Override
    public TopicStateReceiver handleState(JsonNode dataNode, TopicStateReceiver stateReceiver, String sn) throws JsonProcessingException {
        String name = dataNode.fieldNames().next();
        JsonNode childNode = dataNode.findPath(name);
        // Determine if it is payload data based on name.
        if (name.equals(StateDataEnum.PAYLOADS.getDesc())) {
            List<DevicePayloadReceiver> payloadsList = new ArrayList<>();

            Iterator<JsonNode> payloadsNode = childNode.elements();
            while (payloadsNode.hasNext()) {
                DevicePayloadReceiver payloadReceiver = mapper.treeToValue(
                        payloadsNode.next(), DevicePayloadReceiver.class);
                payloadReceiver.setDeviceSn(sn);
                payloadsList.add(payloadReceiver);
            }

            if (payloadsList.isEmpty()) {
                DevicePayloadReceiver payloadReceiver = new DevicePayloadReceiver();
                payloadReceiver.setDeviceSn(sn);
                payloadsList.add(payloadReceiver);
            }

            stateReceiver.setData(payloadsList);
            return stateReceiver;
        }
        return handler.handleState(dataNode, stateReceiver, sn);
    }
}
