package com.dji.sample.control.service;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
public interface IControlService {

    ResponseResult controlDock(String sn, String serviceIdentifier);

    void handleControlProgress(CommonTopicReceiver receiver, MessageHeaders headers);

}
