package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.manage.dao.IWorkspaceMapper;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import com.dji.sample.manage.model.entity.WorkspaceEntity;
import com.dji.sample.manage.service.IWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class WorkspaceServiceImpl implements IWorkspaceService {

    @Autowired
    private IWorkspaceMapper mapper;

    @Override
    public Optional<WorkspaceDTO> getWorkspaceById(int id) {
        return Optional.ofNullable(entityConvertToDto(mapper.selectById(id)));
    }

    @Override
    public Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId) {
        return Optional.ofNullable(entityConvertToDto(
                mapper.selectOne(
                        new LambdaQueryWrapper<WorkspaceEntity>()
                                .eq(WorkspaceEntity::getWorkspaceId, workspaceId))));
    }

    /**
     * Convert database entity objects into workspace data transfer object.
     * @param entity
     * @return
     */
    private WorkspaceDTO entityConvertToDto(WorkspaceEntity entity) {
        WorkspaceDTO.WorkspaceDTOBuilder builder = WorkspaceDTO.builder();
        if (entity == null) {
            return builder.build();
        }
        return builder
                .id(entity.getId())
                .workspaceId(entity.getWorkspaceId())
                .platformName(entity.getPlatformName())
                .workspaceDesc(entity.getWorkspaceDesc())
                .workspaceName(entity.getWorkspaceName())
                .build();
    }
}
