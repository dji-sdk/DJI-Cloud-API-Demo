package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.map.dao.IGroupMapper;
import com.dji.sample.map.model.dto.GroupDTO;
import com.dji.sample.map.model.entity.GroupEntity;
import com.dji.sample.map.service.IGroupElementService;
import com.dji.sample.map.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Service
@Transactional
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private IGroupMapper mapper;

    @Autowired
    private IGroupElementService groupElementService;

    @Override
    public List<GroupDTO> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed) {

        return mapper.selectList(
                new LambdaQueryWrapper<GroupEntity>()
                        .eq(GroupEntity::getWorkspaceId, workspaceId)
                        .eq(StringUtils.hasText(groupId), GroupEntity::getGroupId, groupId)
                        .eq(isDistributed != null, GroupEntity::getIsDistributed, isDistributed))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert database entity objects into group data transfer object.
     * @param entity
     * @return
     */
    private GroupDTO entityConvertToDto(GroupEntity entity) {
        GroupDTO.GroupDTOBuilder builder = GroupDTO.builder();

        if (entity == null) {
            return builder.build();
        }

        return builder
                .id(entity.getGroupId())
                .name(entity.getGroupName())
                .type(entity.getGroupType())
                .isLock(entity.getIsLock())
                .isDistributed(entity.getIsDistributed())
                .createTime(entity.getCreateTime())
                .build();
    }
}
