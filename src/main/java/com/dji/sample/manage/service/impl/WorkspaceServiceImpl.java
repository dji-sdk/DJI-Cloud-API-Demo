package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.manage.dao.IWorkspaceMapper;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import com.dji.sample.manage.model.entity.WorkspaceEntity;
import com.dji.sample.manage.model.receiver.OrganizationGetReceiver;
import com.dji.sample.manage.service.IWorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class WorkspaceServiceImpl implements IWorkspaceService {

    @Autowired
    private IWorkspaceMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IMessageSenderService messageSenderService;

    @Override
    public Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId) {
        return Optional.ofNullable(entityConvertToDto(
                mapper.selectOne(
                        new LambdaQueryWrapper<WorkspaceEntity>()
                                .eq(WorkspaceEntity::getWorkspaceId, workspaceId))));
    }

    @Override
    public Optional<WorkspaceDTO> getWorkspaceNameByBindCode(String bindCode) {
        return Optional.ofNullable(entityConvertToDto(
                mapper.selectOne(new LambdaQueryWrapper<WorkspaceEntity>().eq(WorkspaceEntity::getBindCode, bindCode))));
    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET, outputChannel = ChannelName.OUTBOUND)
    public void replyOrganizationGet(CommonTopicReceiver receiver, MessageHeaders headers) {
        OrganizationGetReceiver organizationGet = objectMapper.convertValue(receiver.getData(), OrganizationGetReceiver.class);
        CommonTopicResponse.CommonTopicResponseBuilder<RequestsReply> builder = CommonTopicResponse.<RequestsReply>builder()
                .tid(receiver.getTid())
                .bid(receiver.getBid())
                .method(RequestsMethodEnum.AIRPORT_ORGANIZATION_GET.getMethod())
                .timestamp(System.currentTimeMillis());

        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString() + TopicConst._REPLY_SUF;

        if (!StringUtils.hasText(organizationGet.getDeviceBindingCode())) {
            builder.data(RequestsReply.error(CommonErrorEnum.ILLEGAL_ARGUMENT));
            messageSenderService.publish(topic, builder.build());
            return;
        }

        Optional<WorkspaceDTO> workspace = this.getWorkspaceNameByBindCode(organizationGet.getDeviceBindingCode());
        if (workspace.isEmpty()) {
            builder.data(RequestsReply.error(CommonErrorEnum.GET_ORGANIZATION_FAILED));
            messageSenderService.publish(topic, builder.build());
            return;
        }

        builder.data(RequestsReply.success(Map.of(MapKeyConst.ORGANIZATION_NAME, workspace.get().getWorkspaceName())));
        messageSenderService.publish(topic, builder.build());
    }

    /**
     * Convert database entity objects into workspace data transfer object.
     * @param entity
     * @return
     */
    private WorkspaceDTO entityConvertToDto(WorkspaceEntity entity) {
        if (entity == null) {
            return null;
        }
        return WorkspaceDTO.builder()
                .id(entity.getId())
                .workspaceId(entity.getWorkspaceId())
                .platformName(entity.getPlatformName())
                .workspaceDesc(entity.getWorkspaceDesc())
                .workspaceName(entity.getWorkspaceName())
                .bindCode(entity.getBindCode())
                .build();
    }
}
