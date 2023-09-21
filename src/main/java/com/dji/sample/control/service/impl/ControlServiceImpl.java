package com.dji.sample.control.service.impl;

import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.control.model.enums.RemoteDebugMethodEnum;
import com.dji.sample.control.model.param.*;
import com.dji.sample.control.service.IControlService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.service.IDevicePayloadService;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sdk.cloudapi.control.FlyToPointRequest;
import com.dji.sdk.cloudapi.control.PayloadAuthorityGrabRequest;
import com.dji.sdk.cloudapi.control.TakeoffToPointRequest;
import com.dji.sdk.cloudapi.control.api.AbstractControlService;
import com.dji.sdk.cloudapi.debug.DebugMethodEnum;
import com.dji.sdk.cloudapi.debug.api.AbstractDebugService;
import com.dji.sdk.cloudapi.device.DockModeCodeEnum;
import com.dji.sdk.cloudapi.device.DroneModeCodeEnum;
import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.cloudapi.wayline.api.AbstractWaylineService;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.SDKManager;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
@Service
@Slf4j
public class ControlServiceImpl implements IControlService {

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IDevicePayloadService devicePayloadService;

    @Autowired
    private AbstractControlService abstractControlService;

    @Autowired
    private AbstractDebugService abstractDebugService;

    @Autowired
    @Qualifier("SDKWaylineService")
    private AbstractWaylineService abstractWaylineService;

    private RemoteDebugHandler checkDebugCondition(String sn, RemoteDebugParam param, RemoteDebugMethodEnum controlMethodEnum) {
        RemoteDebugHandler handler = Objects.nonNull(controlMethodEnum.getClazz()) ?
                mapper.convertValue(Objects.nonNull(param) ? param : new Object(), controlMethodEnum.getClazz())
                : new RemoteDebugHandler();
        if (!handler.canPublish(sn)) {
            throw new RuntimeException("The current state of the dock does not support this function.");
        }
        return handler;
    }

    @Override
    public HttpResultResponse controlDockDebug(String sn, RemoteDebugMethodEnum controlMethodEnum, RemoteDebugParam param) {
        DebugMethodEnum methodEnum = controlMethodEnum.getDebugMethodEnum();
        RemoteDebugHandler data = checkDebugCondition(sn, param, controlMethodEnum);

        boolean isExist = deviceRedisService.checkDeviceOnline(sn);
        if (!isExist) {
            return HttpResultResponse.error("The dock is offline.");
        }
        TopicServicesResponse response;
        switch (controlMethodEnum) {
            case RETURN_HOME:
                response = abstractWaylineService.returnHome(SDKManager.getDeviceSDK(sn));
                break;
            case RETURN_HOME_CANCEL:
                response = abstractWaylineService.returnHomeCancel(SDKManager.getDeviceSDK(sn));
                break;
            default:
                response = abstractDebugService.remoteDebug(SDKManager.getDeviceSDK(sn), methodEnum,
                        Objects.nonNull(methodEnum.getClazz()) ? mapper.convertValue(data, methodEnum.getClazz()) : null);
        }
        ServicesReplyData serviceReply = (ServicesReplyData) response.getData();
        if (!serviceReply.getResult().isSuccess()) {
            return HttpResultResponse.error(serviceReply.getResult());
        }
        return HttpResultResponse.success();
    }

    private void checkFlyToCondition(String dockSn) {
        // TODO 设备固件版本不兼容情况
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("The dock is offline, please restart the dock.");
        }

        DroneModeCodeEnum deviceMode = deviceService.getDeviceMode(dockOpt.get().getChildDeviceSn());
        if (DroneModeCodeEnum.MANUAL != deviceMode) {
            throw new RuntimeException("The current state of the drone does not support this function, please try again later.");
        }

        HttpResultResponse result = seizeAuthority(dockSn, DroneAuthorityEnum.FLIGHT, null);
        if (HttpResultResponse.CODE_SUCCESS != result.getCode()) {
            throw new IllegalArgumentException(result.getMessage());
        }
    }

    @Override
    public HttpResultResponse flyToPoint(String sn, FlyToPointParam param) {
        checkFlyToCondition(sn);

        param.setFlyToId(UUID.randomUUID().toString());
        TopicServicesResponse<ServicesReplyData> response = abstractControlService.flyToPoint(
                SDKManager.getDeviceSDK(sn), mapper.convertValue(param, FlyToPointRequest.class));
        ServicesReplyData reply = response.getData();
        return reply.getResult().isSuccess() ?
                HttpResultResponse.success()
                : HttpResultResponse.error("Flying to the target point failed. " + reply.getResult());
    }

    @Override
    public HttpResultResponse flyToPointStop(String sn) {
        TopicServicesResponse<ServicesReplyData> response = abstractControlService.flyToPointStop(SDKManager.getDeviceSDK(sn));
        ServicesReplyData reply = response.getData();

        return reply.getResult().isSuccess() ?
                HttpResultResponse.success()
                : HttpResultResponse.error("The drone flying to the target point failed to stop. " + reply.getResult());
    }

    private void checkTakeoffCondition(String dockSn) {
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty() || DockModeCodeEnum.IDLE != deviceService.getDockMode(dockSn)) {
            throw new RuntimeException("The current state does not support takeoff.");
        }

        HttpResultResponse result = seizeAuthority(dockSn, DroneAuthorityEnum.FLIGHT, null);
        if (HttpResultResponse.CODE_SUCCESS != result.getCode()) {
            throw new IllegalArgumentException(result.getMessage());
        }

    }

    @Override
    public HttpResultResponse takeoffToPoint(String sn, TakeoffToPointParam param) {
        checkTakeoffCondition(sn);

        param.setFlightId(UUID.randomUUID().toString());
        TopicServicesResponse<ServicesReplyData> response = abstractControlService.takeoffToPoint(
                SDKManager.getDeviceSDK(sn), mapper.convertValue(param, TakeoffToPointRequest.class));
        ServicesReplyData reply = response.getData();
        return reply.getResult().isSuccess() ?
                HttpResultResponse.success()
                : HttpResultResponse.error("The drone failed to take off. " + reply.getResult());
    }

    @Override
    public HttpResultResponse seizeAuthority(String sn, DroneAuthorityEnum authority, DronePayloadParam param) {
        TopicServicesResponse<ServicesReplyData> response;
        switch (authority) {
            case FLIGHT:
                if (deviceService.checkAuthorityFlight(sn)) {
                    return HttpResultResponse.success();
                }
                response = abstractControlService.flightAuthorityGrab(SDKManager.getDeviceSDK(sn));
                break;
            case PAYLOAD:
                if (checkPayloadAuthority(sn, param.getPayloadIndex())) {
                    return HttpResultResponse.success();
                }
                response = abstractControlService.payloadAuthorityGrab(SDKManager.getDeviceSDK(sn),
                        new PayloadAuthorityGrabRequest().setPayloadIndex(new PayloadIndex(param.getPayloadIndex())));
                break;
            default:
                return HttpResultResponse.error(CloudSDKErrorEnum.INVALID_PARAMETER);
        }

        ServicesReplyData serviceReply = response.getData();
        return serviceReply.getResult().isSuccess() ?
                HttpResultResponse.success()
                : HttpResultResponse.error(serviceReply.getResult());
    }

    private Boolean checkPayloadAuthority(String sn, String payloadIndex) {
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(sn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("The dock is offline, please restart the dock.");
        }
        return devicePayloadService.checkAuthorityPayload(dockOpt.get().getChildDeviceSn(), payloadIndex);
    }

    @Override
    public HttpResultResponse payloadCommands(PayloadCommandsParam param) throws Exception {
        param.getCmd().getClazz()
                .getDeclaredConstructor(DronePayloadParam.class)
                .newInstance(param.getData())
                .checkCondition(param.getSn());

        TopicServicesResponse<ServicesReplyData> response = abstractControlService.payloadControl(
                SDKManager.getDeviceSDK(param.getSn()), param.getCmd().getCmd(),
                mapper.convertValue(param.getData(), param.getCmd().getCmd().getClazz()));

        ServicesReplyData serviceReply = response.getData();
        return serviceReply.getResult().isSuccess() ?
                HttpResultResponse.success()
                : HttpResultResponse.error(serviceReply.getResult());
    }
}
