package com.dji.sample.media.service.impl;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.MapKeyConst;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.media.model.FileUploadCallback;
import com.dji.sample.media.model.FileUploadDTO;
import com.dji.sample.media.model.MediaFileCountDTO;
import com.dji.sample.media.model.MediaFileDTO;
import com.dji.sample.media.service.IFileService;
import com.dji.sample.media.service.IMediaService;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Service
@Slf4j
public class MediaServiceImpl implements IMediaService {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IMessageSenderService messageSenderService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ISendMessageService sendMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Override
    public Boolean fastUpload(String workspaceId, String fingerprint) {
        return fileService.checkExist(workspaceId, fingerprint);
    }

    @Override
    public Integer saveMediaFile(String workspaceId, FileUploadDTO file) {
        return fileService.saveFile(workspaceId, file);
    }

    @Override
    public List<String> getAllTinyFingerprintsByWorkspaceId(String workspaceId) {
        return fileService.getAllFilesByWorkspaceId(workspaceId)
                .stream()
                .map(MediaFileDTO::getTinnyFingerprint)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getExistTinyFingerprints(String workspaceId, List<String> tinyFingerprints) {
        List<String> tinyFingerprintList = this.getAllTinyFingerprintsByWorkspaceId(workspaceId);
        return tinyFingerprints
                .stream()
                .filter(tinyFingerprintList::contains)
                .collect(Collectors.toList());

    }

    /**
     * Handle media files messages reported by dock.
     * @param receiver
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleFileUploadCallBack(CommonTopicReceiver receiver) {
        FileUploadCallback callback = objectMapper.convertValue(receiver.getData(), FileUploadCallback.class);

        if (callback.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("Media file upload failed!");
            return null;
        }

        String jobId = callback.getFile().getExt().getFlightId();

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(receiver.getGateway());
        MediaFileCountDTO mediaFileCount = (MediaFileCountDTO) RedisOpsUtils.hashGet(RedisConst.MEDIA_FILE_PREFIX + receiver.getGateway(), jobId);
        // duplicate data
        if (deviceOpt.isEmpty()
                || (Objects.nonNull(mediaFileCount) && receiver.getBid().equals(mediaFileCount.getBid())
                && receiver.getTid().equals(mediaFileCount.getTid()))) {
            return receiver;
        }


        DeviceDTO device = deviceOpt.get();
        Optional<WaylineJobDTO> jobOpt = waylineJobService.getJobByJobId(device.getWorkspaceId(), jobId);
        if (jobOpt.isPresent()) {
            boolean isSave = parseMediaFile(callback, jobOpt.get());
            if (!isSave) {
                log.error("Failed to save the file to the database, please check the data manually.");
                return null;
            }
        }

        notifyUploadedCount(mediaFileCount, receiver, jobId, device);
        return receiver;
    }

    /**
     * update the uploaded count and notify web side
     * @param mediaFileCount
     * @param receiver
     * @param jobId
     */
    private void notifyUploadedCount(MediaFileCountDTO mediaFileCount, CommonTopicReceiver receiver, String jobId, DeviceDTO dock) {
        // Do not notify when files that do not belong to the route are uploaded.
        if (Objects.isNull(mediaFileCount)) {
            return;
        }
        mediaFileCount.setBid(receiver.getBid());
        mediaFileCount.setTid(receiver.getTid());
        mediaFileCount.setUploadedCount(mediaFileCount.getUploadedCount() + 1);

        String key = RedisConst.MEDIA_FILE_PREFIX + receiver.getGateway();
        // After all the files of the job are uploaded, delete the media file key.
        if (mediaFileCount.getUploadedCount() >= mediaFileCount.getMediaCount()) {
            RedisOpsUtils.hashDel(key, new String[]{jobId});

            // After uploading, delete the key with the highest priority.
            String highestKey = RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + receiver.getGateway();
            if (RedisOpsUtils.checkExist(highestKey) &&
                    jobId.equals(((MediaFileCountDTO) RedisOpsUtils.get(highestKey)).getJobId())) {
                RedisOpsUtils.del(highestKey);
            }

            if (RedisOpsUtils.hashLen(key) == 0) {
                RedisOpsUtils.del(key);
            }
        } else {
            RedisOpsUtils.hashSet(key, jobId, mediaFileCount);
        }

        sendMessageService.sendBatch(dock.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                        BizCodeEnum.FILE_UPLOAD_CALLBACK.getCode(), mediaFileCount);
    }

    private Boolean parseMediaFile(FileUploadCallback callback, WaylineJobDTO job) {
        // Set the drone sn that shoots the media
        Optional<DeviceDTO> dockDTO = deviceService.getDeviceBySn(job.getDockSn());
        dockDTO.ifPresent(dock -> callback.getFile().getExt().setSn(dock.getChildDeviceSn()));

        // set path
        String objectKey = callback.getFile().getObjectKey();
        callback.getFile().setPath(objectKey.substring(objectKey.indexOf("/") + 1, objectKey.lastIndexOf("/")));

        return fileService.saveFile(job.getWorkspaceId(), callback.getFile()) > 0;
    }

    /**
     * Handles the highest priority message about media uploads.
     * @param receiver
     * @param headers
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleHighestPriorityUploadFlightTaskMedia(CommonTopicReceiver receiver, MessageHeaders headers) {
        Map map = objectMapper.convertValue(receiver.getData(), Map.class);
        if (map.isEmpty() || !map.containsKey(MapKeyConst.FLIGHT_ID)) {
            return null;
        }

        String dockSn = receiver.getGateway();
        String jobId = map.get(MapKeyConst.FLIGHT_ID).toString();
        String key = RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + dockSn;
        MediaFileCountDTO countDTO = new MediaFileCountDTO();
        if (RedisOpsUtils.checkExist(key)) {
            countDTO = (MediaFileCountDTO) RedisOpsUtils.get(key);
            if (jobId.equals(countDTO.getJobId())) {
                RedisOpsUtils.setWithExpire(key, countDTO, RedisConst.DEVICE_ALIVE_SECOND * 5);
                return null;
            }

            countDTO.setPreJobId(countDTO.getJobId());
        }
        countDTO.setJobId(jobId);

        RedisOpsUtils.setWithExpire(key, countDTO, RedisConst.DEVICE_ALIVE_SECOND * 5);

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(receiver.getGateway());
        if (deviceOpt.isEmpty()) {
            return null;
        }
        sendMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                        BizCodeEnum.HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA.getCode(), countDTO);

        return receiver;
    }
}
