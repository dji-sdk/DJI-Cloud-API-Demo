package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.StateDataEnum;
import com.dji.sample.manage.model.receiver.LiveCapacityReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StateLiveCapacityHandler extends AbstractStateTopicHandler {

    protected StateLiveCapacityHandler(@Autowired @Qualifier("stateFirmwareVersionHandler") AbstractStateTopicHandler handler) {
        super(handler);
    }

    @Override
    public CommonTopicReceiver handleState(Map<String, Object> dataNode, CommonTopicReceiver stateReceiver, String sn) throws JsonProcessingException {
        // Determine if it is live capacity data based on name.
        if (dataNode.containsKey(StateDataEnum.LIVE_CAPACITY.getDesc())) {
            stateReceiver.setData(mapper.convertValue(
                    dataNode.get(StateDataEnum.LIVE_CAPACITY.getDesc()),
                    LiveCapacityReceiver.class));
            log.info("Analyze live stream capabilities.");
            return stateReceiver;
        }
        return handler.handleState(dataNode, stateReceiver, sn);
    }
}
