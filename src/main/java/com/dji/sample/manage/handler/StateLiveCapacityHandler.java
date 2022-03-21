package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.manage.model.enums.StateDataEnum;
import com.dji.sample.manage.model.receiver.CapacityDeviceReceiver;
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
public class StateLiveCapacityHandler extends AbstractStateTopicHandler {

    private static final String DEVICE_LIST = "device_list";

    protected StateLiveCapacityHandler(@Autowired @Qualifier("stateDefaultHandler") AbstractStateTopicHandler handler) {
        super(handler);
    }

    @Override
    public TopicStateReceiver handleState(JsonNode dataNode, TopicStateReceiver stateReceiver, String sn) throws JsonProcessingException {
        String name = dataNode.fieldNames().next();
        JsonNode childNode = dataNode.findPath(name);
        // Determine if it is live capacity data based on name.
        if (name.equals(StateDataEnum.LIVE_CAPACITY.getDesc())) {
            JsonNode deviceNode = childNode.findPath(DEVICE_LIST);
            stateReceiver.setData(
                    mapper.treeToValue(deviceNode.get(0), CapacityDeviceReceiver.class));
            return stateReceiver;
        }
        return handler.handleState(dataNode, stateReceiver, sn);
    }
}
