package com.dji.sample.control.service;

import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.control.model.enums.RemoteDebugMethodEnum;
import com.dji.sample.control.model.param.*;
import com.dji.sdk.common.HttpResultResponse;

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
    HttpResultResponse controlDockDebug(String sn, RemoteDebugMethodEnum serviceIdentifier, RemoteDebugParam param);

    /**
     * Make the drone fly to the target point.
     * @param sn
     * @param param
     * @return
     */
    HttpResultResponse flyToPoint(String sn, FlyToPointParam param);

    /**
     * End the mission of flying the drone to the target point.
     * @param sn
     * @return
     */
    HttpResultResponse flyToPointStop(String sn);

    /**
     * Handle progress result notifications for fly to target point.
     * @param receiver
     * @param headers
     * @return
     */
//    CommonTopicReceiver handleFlyToPointProgress(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Control the drone to take off.
     * @param sn
     * @param param
     * @return
     */
    HttpResultResponse takeoffToPoint(String sn, TakeoffToPointParam param);

    /**
     * Seize the control authority of the drone or the payload control authority.
     * @param sn
     * @param authority
     * @param param
     * @return
     */
    HttpResultResponse seizeAuthority(String sn, DroneAuthorityEnum authority, DronePayloadParam param);

    /**
     * Control the payload of the drone.
     * @param param
     * @return
     */
    HttpResultResponse payloadCommands(PayloadCommandsParam param) throws Exception;
}
