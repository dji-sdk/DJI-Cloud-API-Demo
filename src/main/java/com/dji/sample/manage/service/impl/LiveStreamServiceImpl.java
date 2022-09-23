package com.dji.sample.manage.service.impl;

import com.dji.sample.common.error.LiveErrorEnum;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.mqtt.model.ServiceReply;
import com.dji.sample.component.mqtt.model.ServicesMethodEnum;
import com.dji.sample.component.mqtt.model.StateDataEnum;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.enums.LiveUrlTypeEnum;
import com.dji.sample.manage.model.enums.LiveVideoQualityEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.CapacityDeviceReceiver;
import com.dji.sample.manage.model.receiver.LiveCapacityReceiver;
import com.dji.sample.manage.service.ICapacityCameraService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.manage.service.ILiveStreamService;
import com.dji.sample.manage.service.IWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
@Service
@Transactional
public class LiveStreamServiceImpl implements ILiveStreamService {

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IWorkspaceService workspaceService;

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private RedisOpsUtils redisOps;

    @Override
    public List<CapacityDeviceDTO> getLiveCapacity(String workspaceId) {

        // Query all devices in this workspace.
        List<DeviceDTO> devicesList = deviceService.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.SUB_DEVICE.getVal(), DeviceDomainEnum.DOCK.getVal()))
                        .build());

        // Query the live capability of each drone.
        return devicesList.stream()
                .filter(device -> redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + device.getDeviceSn()))
                .map(device -> CapacityDeviceDTO.builder()
                        .name(device.getDeviceName())
                        .sn(device.getDeviceSn())
                        .camerasList(capacityCameraService.getCapacityCameraByDeviceSn(device.getDeviceSn()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void saveLiveCapacity(LiveCapacityReceiver liveCapacityReceiver, Long timestamp) {
        // Solve timing problems
        for (CapacityDeviceReceiver capacityDeviceReceiver : liveCapacityReceiver.getDeviceList()) {
            long last = (long) Objects.requireNonNullElse(
                    redisOps.get(StateDataEnum.LIVE_CAPACITY + RedisConst.DELIMITER + capacityDeviceReceiver.getSn()), 0L);
            if (last > timestamp) {
                return;
            }
            capacityCameraService.saveCapacityCameraReceiverList(
                    capacityDeviceReceiver.getCameraList(),
                    capacityDeviceReceiver.getSn(), timestamp);
        }
    }

    @Override
    public ResponseResult liveStart(LiveTypeDTO liveParam) {
        // Check if this lens is available live.
        ResponseResult responseResult = this.checkBeforeLive(liveParam.getVideoId());
        if (responseResult.getCode() != 0) {
            return responseResult;
        }

        DeviceDTO data = (DeviceDTO)responseResult.getData();
        // target topic
        String respTopic = THING_MODEL_PRE + PRODUCT +
                data.getDeviceSn() + SERVICES_SUF;
        Optional<ServiceReply> receiveReplyOpt = this.publishLiveStart(respTopic, liveParam);

        if (receiveReplyOpt.isEmpty()) {
            return ResponseResult.error(LiveErrorEnum.NO_REPLY);
        }
        if (receiveReplyOpt.get().getResult() != 0) {
            return ResponseResult.error(LiveErrorEnum.find(receiveReplyOpt.get().getResult()));
        }

        LiveUrlTypeEnum urlType = LiveUrlTypeEnum.find(liveParam.getUrlType());
        LiveDTO live = new LiveDTO();

        switch (urlType) {
            case RTMP:
                live.setUrl(liveParam.getUrl().replace("rtmp", "webrtc"));
                break;
            case GB28181:
                LiveUrlGB28181DTO gb28181 = urlToGB28181(liveParam.getUrl());
                live.setUrl(new StringBuilder()
                        .append("webrtc://")
                        .append(gb28181.getServerIP())
                        .append("/live/")
                        .append(gb28181.getAgentID())
                        .append("@")
                        .append(gb28181.getChannel())
                        .toString());
                break;
            case RTSP:
                String url = receiveReplyOpt.get().getInfo().toString();
                this.resolveUrlUser(url, live);
                break;
            case UNKNOWN:
                return ResponseResult.error(LiveErrorEnum.URL_TYPE_NOT_SUPPORTED);
        }

        return ResponseResult.success(live);
    }

    @Override
    public ResponseResult liveStop(String videoId) {
        ResponseResult<DeviceDTO> responseResult = this.checkBeforeLive(videoId);
        if (responseResult.getCode() != 0) {
            return responseResult;
        }

        String respTopic = THING_MODEL_PRE + PRODUCT + responseResult.getData().getDeviceSn() + SERVICES_SUF;

        Optional<ServiceReply> receiveReplyOpt = this.publishLiveStop(respTopic, videoId);
        if (receiveReplyOpt.isEmpty()) {
            return ResponseResult.error(LiveErrorEnum.NO_REPLY);
        }
        if (receiveReplyOpt.get().getResult() != 0) {
            return ResponseResult.error(LiveErrorEnum.find(receiveReplyOpt.get().getResult()));
        }

        return ResponseResult.success();
    }

    @Override
    public ResponseResult liveSetQuality(LiveTypeDTO liveParam) {
        if (liveParam.getVideoQuality() == null ||
                LiveVideoQualityEnum.UNKNOWN == LiveVideoQualityEnum.find(liveParam.getVideoQuality())) {
            return ResponseResult.error(LiveErrorEnum.ERROR_PARAMETERS);
        }

        ResponseResult<DeviceDTO> responseResult = this.checkBeforeLive(liveParam.getVideoId());

        if (responseResult.getCode() != 0) {
            return responseResult;
        }

        String respTopic = THING_MODEL_PRE + PRODUCT + responseResult.getData().getDeviceSn() + SERVICES_SUF;

        Optional<ServiceReply> receiveReplyOpt = this.publishLiveSetQuality(respTopic, liveParam);
        if (receiveReplyOpt.isEmpty()) {
            return ResponseResult.error(LiveErrorEnum.NO_REPLY);
        }
        if (receiveReplyOpt.get().getResult() != 0) {
            return ResponseResult.error(LiveErrorEnum.find(receiveReplyOpt.get().getResult()));
        }

        return ResponseResult.success();
    }

    /**
     * Check if this lens is available live.
     * @param videoId
     * @return
     */
    private ResponseResult<DeviceDTO> checkBeforeLive(String videoId) {
        String[] videoIdArr = videoId.split("/");
        // drone sn / enumeration value of the location where the payload is mounted / payload lens
        if (videoIdArr.length != 3) {
            return ResponseResult.error(LiveErrorEnum.ERROR_PARAMETERS);
        }

        Optional<DeviceDTO> deviceOpt = deviceService.getDeviceBySn(videoIdArr[0]);
        // Check if the gateway device connected to this drone exists
        if (deviceOpt.isEmpty()) {
            return ResponseResult.error(LiveErrorEnum.NO_AIRCRAFT);
        }

        if (deviceOpt.get().getDomain().equals(DeviceDomainEnum.DOCK.getDesc())) {
            return ResponseResult.success(deviceOpt.get());
        }
        List<DeviceDTO> gatewayList = deviceService.getDevicesByParams(
                DeviceQueryParam.builder()
                        .childSn(videoIdArr[0])
                        .build());
        if (gatewayList.isEmpty()) {
            return ResponseResult.error(LiveErrorEnum.NO_FLIGHT_CONTROL);
        }

        return ResponseResult.success(gatewayList.get(0));
    }

    /**
     * When using rtsp live, the account and password are parsed from the information returned by the pilot.
     * @param url
     * @param live
     */
    private void resolveUrlUser(String url, LiveDTO live) {
        if (!StringUtils.hasText(url)) {
            return;
        }
        int start = url.indexOf("//");
        int end = url.lastIndexOf("@");
        String user = url.substring(start + 2, end);

        url = url.replace(user + "@", "");
        String[] userArr = user.split(":");
        live.setUsername(userArr[0]);
        live.setPassword(userArr[1]);
        live.setUrl(url);
    }

    /**
     * When using GB28181 live, url parameters are resolved into objects.
     * @param url
     * @return
     */
    private LiveUrlGB28181DTO urlToGB28181(String url) {
        String[] arr = url.split("\\=|\\&");
        LiveUrlGB28181DTO gb28181 = new LiveUrlGB28181DTO();
        try {
            Class<LiveUrlGB28181DTO> clazz = LiveUrlGB28181DTO.class;
            for (int i = 0; i < arr.length - 1; i += 2) {
                Field field = clazz.getDeclaredField(arr[i]);
                field.setAccessible(true);

                if (field.getType().equals(Integer.class)) {
                    field.set(gb28181, Integer.valueOf(arr[i + 1]));
                    continue;
                }
                field.set(gb28181, arr[i + 1]);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return gb28181;
    }

    /**
     * Send a message to the pilot via mqtt to start the live streaming.
     * @param topic
     * @param liveParam
     * @return
     */
    private Optional<ServiceReply> publishLiveStart(String topic, LiveTypeDTO liveParam) {
        CommonTopicResponse<LiveTypeDTO> response = new CommonTopicResponse<>();
        response.setTid(UUID.randomUUID().toString());
        response.setBid(UUID.randomUUID().toString());
        response.setData(liveParam);
        response.setMethod(ServicesMethodEnum.LIVE_START_PUSH.getMethod());

        return messageSender.publishWithReply(topic, response);
    }

    /**
     * Send a message to the pilot via mqtt to set quality.
     * @param respTopic
     * @param liveParam
     * @return
     */
    private Optional<ServiceReply> publishLiveSetQuality(String respTopic, LiveTypeDTO liveParam) {
        Map<String, Object> data = new ConcurrentHashMap<>(Map.of(
                "video_id", liveParam.getVideoId(),
                "video_quality", liveParam.getVideoQuality()));
        CommonTopicResponse<Map<String, Object>> response = new CommonTopicResponse<>();
        response.setTid(UUID.randomUUID().toString());
        response.setBid(UUID.randomUUID().toString());
        response.setMethod(ServicesMethodEnum.LIVE_SET_QUALITY.getMethod());
        response.setData(data);

        return messageSender.publishWithReply(respTopic, response);
    }

    /**
     * Send a message to the pilot via mqtt to stop the live streaming.
     * @param topic
     * @param videoId
     * @return
     */
    private Optional<ServiceReply> publishLiveStop(String topic, String videoId) {
        Map<String, String> data = new ConcurrentHashMap<>(Map.of("video_id", videoId));
        CommonTopicResponse<Map<String, String>> response = new CommonTopicResponse<>();
        response.setTid(UUID.randomUUID().toString());
        response.setBid(UUID.randomUUID().toString());
        response.setData(data);
        response.setMethod(ServicesMethodEnum.LIVE_STOP_PUSH.getMethod());

        return messageSender.publishWithReply(topic, response);
    }

}