package com.dji.sample.control.service;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.control.model.param.RemoteDebugParam;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
public interface IControlService {

    /**
     * Remotely debug the dock via commands.
     * @param sn
     * @param serviceIdentifier
     * @param param
     * @return
     */
    ResponseResult controlDock(String sn, String serviceIdentifier, RemoteDebugParam param);

    /**
     * Handles multi-state command progress information.
     * @param receiver
     * @param headers
     */
    void handleControlProgress(CommonTopicReceiver receiver, MessageHeaders headers);

}
