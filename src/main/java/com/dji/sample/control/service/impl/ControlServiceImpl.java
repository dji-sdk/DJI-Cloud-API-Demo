package com.dji.sample.control.service.impl;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.control.model.dto.FlyToProgressReceiver;
import com.dji.sample.control.model.dto.ResultNotifyDTO;
import com.dji.sample.control.model.dto.TakeoffProgressReceiver;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.control.model.enums.DroneControlMethodEnum;
import com.dji.sample.control.model.enums.RemoteDebugMethodEnum;
import com.dji.sample.control.model.param.*;
import com.dji.sample.control.service.IControlService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.DeviceModeCodeEnum;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.IDevicePayloadService;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.wayline.model.enums.WaylineErrorCodeEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
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
    private IMessageSenderService messageSenderService;

    @Autowired
    private ISendMessageService webSocketMessageService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IDevicePayloadService devicePayloadService;

    private RemoteDebugHandler checkDebugCondition(String sn, RemoteDebugParam param, RemoteDebugMethodEnum controlMethodEnum) {
        RemoteDebugHandler handler = Objects.nonNull(controlMethodEnum.getClazz()) ?
                mapper.convertValue(Objects.nonNull(param) ? param : new Object(), controlMethodEnum.getClazz())
                : new RemoteDebugHandler();
        if (!handler.canPublish(sn)) {
            throw new RuntimeException("The current state of the dock does not support this function.");
        }
        if (Objects.nonNull(param) && !handler.valid()) {
            throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getErrorMsg());
        }
        return handler;
    }

    @Override
    public ResponseResult controlDockDebug(String sn, String serviceIdentifier, RemoteDebugParam param) {
        RemoteDebugMethodEnum controlMethodEnum = RemoteDebugMethodEnum.find(serviceIdentifier);
        if (RemoteDebugMethodEnum.UNKNOWN == controlMethodEnum) {
            return ResponseResult.error("The " + serviceIdentifier + " method does not exist.");
        }

        RemoteDebugHandler data = checkDebugCondition(sn, param, controlMethodEnum);

        boolean isExist = deviceRedisService.checkDeviceOnline(sn);
        if (!isExist) {
            return ResponseResult.error("The dock is offline.");
        }
        String bid = UUID.randomUUID().toString();
        ServiceReply serviceReply = messageSenderService.publishServicesTopic(sn, serviceIdentifier, data, bid);

        if (ResponseResult.CODE_SUCCESS != serviceReply.getResult()) {
            return ResponseResult.error(serviceReply.getResult(),
                    "error: " + serviceIdentifier + serviceReply.getResult());
        }
        if (controlMethodEnum.getProgress()) {
            RedisOpsUtils.setWithExpire(serviceIdentifier + RedisConst.DELIMITER +  bid, sn,
                    RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
        }
        return ResponseResult.success();
    }

    /**
     * Handles multi-state command progress information.
     * @param receiver
     * @param headers
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleControlProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String key = receiver.getMethod() + RedisConst.DELIMITER + receiver.getBid();
        if (RedisOpsUtils.getExpire(key) <= 0) {
            return receiver;
        }
        String sn = RedisOpsUtils.get(key).toString();

        EventsReceiver<EventsOutputProgressReceiver> eventsReceiver = mapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<EventsOutputProgressReceiver>>(){});
        eventsReceiver.setBid(receiver.getBid());
        eventsReceiver.setSn(sn);

        log.info("SN: {}, {} ===> Control progress: {}",
                sn, receiver.getMethod(), eventsReceiver.getOutput().getProgress().toString());

        if (eventsReceiver.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("SN: {}, {} ===> Error code: {}", sn, receiver.getMethod(), eventsReceiver.getResult());
        }

        if (eventsReceiver.getOutput().getProgress().getPercent() == 100 ||
                EventsResultStatusEnum.find(eventsReceiver.getOutput().getStatus()).getEnd()) {
            RedisOpsUtils.del(key);
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(sn);

        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("The device is offline.");
        }

        DeviceDTO device = deviceOpt.get();
        webSocketMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                receiver.getMethod(), eventsReceiver);

        return receiver;
    }

    private void checkFlyToCondition(String dockSn) {
        // TODO 设备固件版本不兼容情况
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("The dock is offline, please restart the dock.");
        }

        DeviceModeCodeEnum deviceMode = deviceService.getDeviceMode(dockOpt.get().getChildDeviceSn());
        if (DeviceModeCodeEnum.MANUAL != deviceMode) {
            throw new RuntimeException("The current state of the drone does not support this function, please try again later.");
        }

        ResponseResult result = seizeAuthority(dockSn, DroneAuthorityEnum.FLIGHT, null);
        if (ResponseResult.CODE_SUCCESS != result.getCode()) {
            throw new IllegalArgumentException(result.getMessage());
        }
    }

    @Override
    public ResponseResult flyToPoint(String sn, FlyToPointParam param) {
        checkFlyToCondition(sn);

        param.setFlyToId(UUID.randomUUID().toString());
        ServiceReply reply = messageSenderService.publishServicesTopic(sn, DroneControlMethodEnum.FLY_TO_POINT.getMethod(), param, param.getFlyToId());
        return ResponseResult.CODE_SUCCESS != reply.getResult() ?
                ResponseResult.error("Flying to the target point failed." + reply.getResult())
                : ResponseResult.success();
    }

    @Override
    public ResponseResult flyToPointStop(String sn) {
        ServiceReply reply = messageSenderService.publishServicesTopic(sn, DroneControlMethodEnum.FLY_TO_POINT_STOP.getMethod(), null);
        return ResponseResult.CODE_SUCCESS != reply.getResult() ?
                ResponseResult.error("The drone flying to the target point failed to stop. " + reply.getResult())
                : ResponseResult.success();
    }

    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLY_TO_POINT_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleFlyToPointProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String dockSn  = receiver.getGateway();

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            log.error("The dock is offline.");
            return null;
        }

        FlyToProgressReceiver eventsReceiver = mapper.convertValue(receiver.getData(), new TypeReference<FlyToProgressReceiver>(){});
        webSocketMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.FLY_TO_POINT_PROGRESS.getCode(),
                ResultNotifyDTO.builder().sn(dockSn)
                        .message(WaylineErrorCodeEnum.SUCCESS == eventsReceiver.getResult() ?
                                eventsReceiver.getStatus().getMessage() : eventsReceiver.getResult().getErrorMsg())
                        .result(eventsReceiver.getResult().getErrorCode())
                        .build());
        return receiver;
    }

    private void checkTakeoffCondition(String dockSn) {
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty() || DockModeCodeEnum.IDLE != deviceService.getDockMode(dockSn)) {
            throw new RuntimeException("The current state does not support takeoff.");
        }

        ResponseResult result = seizeAuthority(dockSn, DroneAuthorityEnum.FLIGHT, null);
        if (ResponseResult.CODE_SUCCESS != result.getCode()) {
            throw new IllegalArgumentException(result.getMessage());
        }

    }

    @Override
    public ResponseResult takeoffToPoint(String sn, TakeoffToPointParam param) {
        checkTakeoffCondition(sn);

        param.setFlightId(UUID.randomUUID().toString());
        ServiceReply reply = messageSenderService.publishServicesTopic(sn, DroneControlMethodEnum.TAKE_OFF_TO_POINT.getMethod(), param, param.getFlightId());
        return ResponseResult.CODE_SUCCESS != reply.getResult() ?
                ResponseResult.error("The drone failed to take off. " + reply.getResult())
                : ResponseResult.success();
    }

    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_TAKE_OFF_TO_POINT_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleTakeoffToPointProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String dockSn  = receiver.getGateway();

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            log.error("The dock is offline.");
            return null;
        }
        TakeoffProgressReceiver eventsReceiver = mapper.convertValue(receiver.getData(), new TypeReference<TakeoffProgressReceiver>(){});

        webSocketMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.TAKE_OFF_TO_POINT_PROGRESS.getCode(),
                ResultNotifyDTO.builder().sn(dockSn)
                        .message(WaylineErrorCodeEnum.SUCCESS == eventsReceiver.getResult() ?
                                eventsReceiver.getStatus().getMessage() : eventsReceiver.getResult().getErrorMsg())
                        .result(eventsReceiver.getResult().getErrorCode())
                        .build());

        return receiver;
    }

    @Override
    public ResponseResult seizeAuthority(String sn, DroneAuthorityEnum authority, DronePayloadParam param) {
        String method;
        switch (authority) {
            case FLIGHT:
                if (deviceService.checkAuthorityFlight(sn)) {
                    return ResponseResult.success();
                }
                method = DroneControlMethodEnum.FLIGHT_AUTHORITY_GRAB.getMethod();
                break;
            case PAYLOAD:
                if (checkPayloadAuthority(sn, param.getPayloadIndex())) {
                    return ResponseResult.success();
                }
                method = DroneControlMethodEnum.PAYLOAD_AUTHORITY_GRAB.getMethod();
                break;
            default:
                return ResponseResult.error(CommonErrorEnum.ILLEGAL_ARGUMENT);
        }
        ServiceReply serviceReply = messageSenderService.publishServicesTopic(sn, method, param);
        return ResponseResult.CODE_SUCCESS != serviceReply.getResult() ?
                ResponseResult.error(serviceReply.getResult(), "Method: " + method + " Error Code:" + serviceReply.getResult())
                : ResponseResult.success();
    }

    private Boolean checkPayloadAuthority(String sn, String payloadIndex) {
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(sn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("The dock is offline, please restart the dock.");
        }
        return devicePayloadService.checkAuthorityPayload(dockOpt.get().getChildDeviceSn(), payloadIndex);
    }


    @Override
    public ResponseResult payloadCommands(PayloadCommandsParam param) throws Exception {
        param.getCmd().getClazz()
                .getDeclaredConstructor(DronePayloadParam.class)
                .newInstance(param.getData())
                .checkCondition(param.getSn());

        ServiceReply serviceReply = messageSenderService.publishServicesTopic(param.getSn(), param.getCmd().getCmd(), param.getData());
        return ResponseResult.CODE_SUCCESS != serviceReply.getResult() ?
                ResponseResult.error(serviceReply.getResult(), " Error Code:" + serviceReply.getResult())
                : ResponseResult.success();
    }

}
