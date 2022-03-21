package com.dji.sample.wayline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.oss.model.AliyunOSSConfiguration;
import com.dji.sample.component.oss.model.MinIOConfiguration;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.component.oss.service.impl.AliyunOssServiceImpl;
import com.dji.sample.component.oss.service.impl.MinIOServiceImpl;
import com.dji.sample.wayline.dao.IWaylineFileMapper;
import com.dji.sample.wayline.model.WaylineFileDTO;
import com.dji.sample.wayline.model.WaylineFileEntity;
import com.dji.sample.wayline.model.WaylineQueryParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Service
@Transactional
public class WaylineFileServiceImpl implements IWaylineFileService {

    @Autowired
    private IWaylineFileMapper mapper;

    private IOssService ossService;

    @Autowired
    private void setOssService(@Autowired AliyunOssServiceImpl aliyunOssService, @Autowired MinIOServiceImpl minIOService) {
        if (AliyunOSSConfiguration.enable) {
            this.ossService = aliyunOssService;
            return;
        }
        if (MinIOConfiguration.enable) {
            this.ossService = minIOService;
            return;
        }
        throw new NullPointerException("ossService is null.");
    }

    @Override
    public PaginationData<WaylineFileDTO> getWaylinesByParam(String workspaceId, WaylineQueryParam param) {
        // Paging Query
        Page<WaylineFileEntity> page = mapper.selectPage(
                new Page<WaylineFileEntity>(param.getPage(), param.getPageSize()),
                new LambdaQueryWrapper<WaylineFileEntity>()
                        .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                        .eq(param.isFavorited(), WaylineFileEntity::getFavorited, param.isFavorited())
                        .and(param.getTemplateType() != null, wrapper ->  {
                                for (Integer type : param.getTemplateType()) {
                                    wrapper.like(WaylineFileEntity::getTemplateTypes, type).or();
                                }
                        })
                        // There is a risk of SQL injection
                        .last(StringUtils.hasText(param.getOrderBy()), " order by " + param.getOrderBy()));

        // Wrap the results of a paging query into a custom paging object.
        List<WaylineFileDTO> records = page.getRecords()
                .stream()
                .map(this::entityConvertToDTO)
                .collect(Collectors.toList());

        return new PaginationData<>(records, new Pagination(page));
    }

    @Override
    public WaylineFileDTO getWaylineByWaylineId(String workspaceId, String waylineId) {
        return this.entityConvertToDTO(
                mapper.selectOne(
                        new LambdaQueryWrapper<WaylineFileEntity>()
                                .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                                .eq(WaylineFileEntity::getWaylineId, waylineId)));
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {
        return ossService.getObjectUrl(bucket, objectKey);
    }

    @Override
    public Integer saveWaylineFile(String workspaceId, WaylineFileDTO metadata) {
        WaylineFileEntity file = this.dtoConvertToEntity(metadata);
        file.setWaylineId(UUID.randomUUID().toString());
        file.setWorkspaceId(workspaceId);

        int insertId = mapper.insert(file);
        return insertId > 0 ? file.getId() : insertId;
    }

    @Override
    public Boolean markFavorite(String workspaceId, List<String> waylineIds, Boolean isFavorite) {
        if (waylineIds.isEmpty()) {
            return false;
        }
        if (isFavorite == null) {
            return true;
        }
        return mapper.update(null, new LambdaUpdateWrapper<WaylineFileEntity>()
                .set(WaylineFileEntity::getFavorited, isFavorite)
                .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                .in(WaylineFileEntity::getWaylineId, waylineIds)) > 0;
    }

    @Override
    public List<String> getDuplicateNames(String workspaceId, List<String> names) {
        return mapper.selectList(new LambdaQueryWrapper<WaylineFileEntity>()
                .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                .in(WaylineFileEntity::getName, names))
                .stream()
                .map(WaylineFileEntity::getName)
                .collect(Collectors.toList());
    }

    /**
     * Convert database entity objects into wayline data transfer object.
     * @param entity
     * @return
     */
    private WaylineFileDTO entityConvertToDTO(WaylineFileEntity entity) {
        WaylineFileDTO.WaylineFileDTOBuilder builder = WaylineFileDTO.builder();

        if (entity != null) {
            builder.droneModelKey(entity.getDroneModelKey())
                    .favorited(entity.getFavorited())
                    .name(entity.getName())
                    .payloadModelKeys(entity.getPayloadModelKeys() != null ?
                            Arrays.asList(entity.getPayloadModelKeys().split(",")) : null)
                    .templateTypes(Arrays.stream(entity.getTemplateTypes().split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()))
                    .username(entity.getUsername())
                    .objectKey(entity.getObjectKey())
                    .updateTime(entity.getUpdateTime())
                    .waylineId(entity.getWaylineId());
        }

        return builder.build();
    }

    /**
     * Convert the received wayline object into a database entity object.
     * @param file
     * @return
     */
    private WaylineFileEntity dtoConvertToEntity(WaylineFileDTO file) {
        WaylineFileEntity.WaylineFileEntityBuilder builder = WaylineFileEntity.builder();

        if (file != null) {
            builder.droneModelKey(file.getDroneModelKey())
                    .name(file.getName())
                    .username(file.getUsername())
                    .objectKey(file.getObjectKey())
                    // Separate multiple payload data with ",".
                    .payloadModelKeys(String.join(",", file.getPayloadModelKeys()))
                    .templateTypes(file.getTemplateTypes().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .favorited(file.getFavorited())
                    .build();
        }

        return builder.build();
    }
}
