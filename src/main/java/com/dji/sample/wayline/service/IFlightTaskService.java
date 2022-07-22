package com.dji.sample.wayline.service;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
public interface IFlightTaskService {

    /**
     * Handle the progress messages of the flight tasks reported by the dock.
     * @param receiver
     */
    void handleProgress(CommonTopicReceiver receiver, MessageHeaders headers);
}
