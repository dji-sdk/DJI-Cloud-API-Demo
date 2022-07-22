package com.dji.sample.media.service.impl;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.media.model.FileUploadCallback;
import com.dji.sample.media.model.FileUploadDTO;
import com.dji.sample.media.model.MediaFileDTO;
import com.dji.sample.media.service.IFileService;
import com.dji.sample.media.service.IMediaService;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Service
public class MediaServiceImpl implements IMediaService {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IMessageSenderService messageSenderService;

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

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK, outputChannel = ChannelName.OUTBOUND)
    public void handleFileUploadCallBack(CommonTopicReceiver receiver) {
        FileUploadCallback callback = objectMapper.convertValue(receiver.getData(), FileUploadCallback.class);

        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + receiver.getGateway()
                + TopicConst.EVENTS_SUF + TopicConst._REPLY_SUF;
        CommonTopicResponse<Object> data = CommonTopicResponse.builder()
                .timestamp(System.currentTimeMillis())
                .method(EventsMethodEnum.FILE_UPLOAD_CALLBACK.getMethod())
                .data(ResponseResult.success())
                .tid(receiver.getTid())
                .bid(receiver.getBid())
                .build();
        if (callback.getResult() == ResponseResult.CODE_SUCCESS) {
            String jobId = callback.getFile().getExt().getFlightId();
            Optional<WaylineJobDTO> jobOpt = waylineJobService.getJobByJobId(jobId);
            if (jobOpt.isPresent()) {
                int id = fileService.saveFile(jobOpt.get().getWorkspaceId(), callback.getFile());
                if (id <= 0) {
                    data.setData(ResponseResult.error());
                }
            }
        }

        messageSenderService.publish(topic, data);
    }
}
