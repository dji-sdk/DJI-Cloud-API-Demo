package com.dji.sample.control.service;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.control.model.param.*;
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
    ResponseResult controlDockDebug(String sn, String serviceIdentifier, RemoteDebugParam param);

    /**
     * Make the drone fly to the target point.
     * @param sn
     * @param param
     * @return
     */
    ResponseResult flyToPoint(String sn, FlyToPointParam param);

    /**
     * End the mission of flying the drone to the target point.
     * @param sn
     * @return
     */
    ResponseResult flyToPointStop(String sn);

    /**
     * Handle progress result notifications for fly to target point.
     * @param receiver
     * @param headers
     * @return
     */
    CommonTopicReceiver handleFlyToPointProgress(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Control the drone to take off.
     * @param sn
     * @param param
     * @return
     */
    ResponseResult takeoffToPoint(String sn, TakeoffToPointParam param);

    /**
     * Handle progress result notifications for takeoff to target point.
     * @param receiver
     * @param headers
     * @return
     */
    CommonTopicReceiver handleTakeoffToPointProgress(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Seize the control authority of the drone or the payload control authority.
     * @param sn
     * @param authority
     * @param param
     * @return
     */
    ResponseResult seizeAuthority(String sn, DroneAuthorityEnum authority, DronePayloadParam param);

    /**
     * Control the payload of the drone.
     * @param param
     * @return
     */
    ResponseResult payloadCommands(PayloadCommandsParam param) throws Exception;
}
