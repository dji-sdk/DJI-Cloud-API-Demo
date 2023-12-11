package com.dji.sdk.cloudapi.control.api;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.control.*;
import com.dji.sdk.common.BaseModel;
import com.dji.sdk.common.Common;
import com.dji.sdk.common.SpringBeanUtils;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.drc.DrcDownPublish;
import com.dji.sdk.mqtt.drc.DrcUpData;
import com.dji.sdk.mqtt.drc.TopicDrcRequest;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.dji.sdk.mqtt.events.TopicEventsResponse;
import com.dji.sdk.mqtt.services.ServicesPublish;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public abstract class AbstractControlService {

    @Resource
    private ServicesPublish servicesPublish;

    @Resource
    private DrcDownPublish drcDownPublish;

    /**
     * Event notification of flyto result
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLY_TO_POINT_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> flyToPointProgress(TopicEventsRequest<FlyToPointProgress> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("flyToPointProgress not implemented");
    }

    /**
     * Event notification of one-key taking off result
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_TAKEOFF_TO_POINT_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> takeoffToPointProgress(TopicEventsRequest<TakeoffToPointProgress> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("takeoffToPointProgress not implemented");
    }

    /**
     * Notification of DRC link state
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_DRC_STATUS_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> drcStatusNotify(TopicEventsRequest<DrcStatusNotify> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("drcStatusNotify not implemented");
    }

    /**
     * Reason notification of invalid Joystick control
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_JOYSTICK_INVALID_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> joystickInvalidNotify(TopicEventsRequest<JoystickInvalidNotify> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("joystickInvalidNotify not implemented");
    }

    /**
     * Flight control authority grabbing
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flightAuthorityGrab(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLIGHT_AUTHORITY_GRAB.getMethod());
    }

    /**
     * Payload control authority grabbing
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> payloadAuthorityGrab(GatewayManager gateway, PayloadAuthorityGrabRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.PAYLOAD_AUTHORITY_GRAB.getMethod(),
                request);
    }

    /**
     * Enter the live flight controls mode
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> drcModeEnter(GatewayManager gateway, DrcModeEnterRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRC_MODE_ENTER.getMethod(),
                request);
    }

    /**
     * Exit the live flight controls mode
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> drcModeExit(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRC_MODE_EXIT.getMethod());
    }

    /**
     * One-key taking off
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> takeoffToPoint(GatewayManager gateway, TakeoffToPointRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.TAKEOFF_TO_POINT.getMethod(),
                request);
    }

    /**
     * flyto target point
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flyToPoint(GatewayManager gateway, FlyToPointRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLY_TO_POINT.getMethod(),
                request);
    }

    /**
     * End the task of flying to target point
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flyToPointStop(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLY_TO_POINT_STOP.getMethod());
    }

    /**
     * Payload control - switch the camera mode
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraModeSwitch(GatewayManager gateway, CameraModeSwitchRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_MODE_SWITCH.getMethod(),
                request);
    }

    /**
     * Payload control - take single photo
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraPhotoTake(GatewayManager gateway, CameraPhotoTakeRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_PHOTO_TAKE.getMethod(),
                request);
    }

    /**
     * Payload control - start recording
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraRecordingStart(GatewayManager gateway, CameraRecordingStartRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_RECORDING_START.getMethod(),
                request);
    }

    /**
     * Payload control - stop recording
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraRecordingStop(GatewayManager gateway, CameraRecordingStopRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_RECORDING_STOP.getMethod(),
                request);
    }

    /**
     * Payload control - double tab to become AIM
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraAim(GatewayManager gateway, CameraAimRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_AIM.getMethod(),
                request);
    }

    /**
     * Payload control - zoom
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraFocalLengthSet(GatewayManager gateway, CameraFocalLengthSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_FOCAL_LENGTH_SET.getMethod(),
                request);
    }

    /**
     * Payload control - reset the gimbal
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> gimbalReset(GatewayManager gateway, GimbalResetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.GIMBAL_RESET.getMethod(),
                request);
    }

    /**
     * Payload control
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    public TopicServicesResponse<ServicesReplyData> payloadControl(GatewayManager gateway, PayloadControlMethodEnum methodEnum, BaseModel request) {
        try {
            AbstractControlService abstractControlService = SpringBeanUtils.getBean(this.getClass());
            Method method = abstractControlService.getClass().getDeclaredMethod(
                    Common.convertSnake(methodEnum.getPayloadMethod().getMethod()),GatewayManager.class, methodEnum.getClazz());
            return (TopicServicesResponse<ServicesReplyData>) method.invoke(abstractControlService, gateway, request);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new CloudSDKException(e);
        } catch (InvocationTargetException e) {
            throw new CloudSDKException(e.getTargetException());
        }
    }


    /**
     * DRC-flight control
     * @param gateway
     * @param request   data
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    protected void droneControlDown(GatewayManager gateway, DroneControlRequest request) {
        drcDownPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRONE_CONTROL.getMethod(),
                request);
    }

    /**
     * Drc up notification of drone control result
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_DRONE_CONTROL)
    public void droneControlUp(TopicDrcRequest<DrcUpData<DroneControlResponse>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("droneControlUp not implemented");
    }

    /**
     * DRC-drone emergency stop
     * @param gateway
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public void droneEmergencyStopDown(GatewayManager gateway) {
        drcDownPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRONE_EMERGENCY_STOP.getMethod());
    }

    /**
     * Drc up notification of drone emergency stop result
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_DRONE_EMERGENCY_STOP)
    public void droneEmergencyStopUp(TopicDrcRequest<DrcUpData> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("droneEmergencyStopUp not implemented");
    }


    /**
     * DRC-heart beat
     * @param gateway
     * @param request   data
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public void heartBeatDown(GatewayManager gateway, HeartBeatRequest request) {
        drcDownPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.HEART_BEAT.getMethod(),
                request);
    }

    /**
     * Drc up notification of heart beat result
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_HEART_BEAT)
    public void heartBeatUp(TopicDrcRequest<HeartBeatRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("heartBeatUp not implemented");
    }

    /**
     * DRC-obstacle avoidance information pushing
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_HSI_INFO_PUSH)
    public void hsiInfoPush(TopicDrcRequest<HsiInfoPush> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("hsiInfoPush not implemented");
    }

    /**
     * DRC-delay information pushing of image transmission link
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_DELAY_INFO_PUSH)
    public void delayInfoPush(TopicDrcRequest<DelayInfoPush> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("delayInfoPush not implemented");
    }

    /**
     * DRC-high frequency osd information pushing
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_OSD_INFO_PUSH)
    public void osdInfoPush(TopicDrcRequest<OsdInfoPush> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdInfoPush not implemented");
    }


}
