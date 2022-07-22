package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.StateDataEnum;
import com.dji.sample.manage.model.receiver.DeviceBasicReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
@Service
public class StateDeviceBasicHandler extends AbstractStateTopicHandler {

    public StateDeviceBasicHandler(@Autowired @Qualifier("stateLiveCapacityHandler") AbstractStateTopicHandler handler) {
        super(handler);
    }

    @Override
    public CommonTopicReceiver handleState(Map<String, Object> dataNode, CommonTopicReceiver stateReceiver, String sn) throws JsonProcessingException {
        // handle device basic data
        if (dataNode.containsKey(StateDataEnum.PAYLOADS.getDesc())) {
            DeviceBasicReceiver data = mapper.convertValue(stateReceiver.getData(), DeviceBasicReceiver.class);
            data.setDeviceSn(sn);
            data.getPayloads().forEach(payload -> payload.setDeviceSn(sn));

            stateReceiver.setData(data);
            return stateReceiver;
        }
        return handler.handleState(dataNode, stateReceiver, sn);
    }
}
