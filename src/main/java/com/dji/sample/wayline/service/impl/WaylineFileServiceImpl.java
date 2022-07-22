package com.dji.sample.wayline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.wayline.dao.IWaylineFileMapper;
import com.dji.sample.wayline.model.dto.WaylineFileDTO;
import com.dji.sample.wayline.model.entity.WaylineFileEntity;
import com.dji.sample.wayline.model.param.WaylineQueryParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private OssServiceContext ossService;

    @Autowired
    private OssConfiguration configuration;

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
    public Optional<WaylineFileDTO> getWaylineByWaylineId(String workspaceId, String waylineId) {
        return Optional.ofNullable(
                this.entityConvertToDTO(
                        mapper.selectOne(
                                new LambdaQueryWrapper<WaylineFileEntity>()
                                    .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                                    .eq(WaylineFileEntity::getWaylineId, waylineId))));
    }

    @Override
    public URL getObjectUrl(String workspaceId, String waylineId) throws SQLException {
        Optional<WaylineFileDTO> waylineOpt = this.getWaylineByWaylineId(workspaceId, waylineId);
        if (waylineOpt.isEmpty()) {
            throw new SQLException(waylineId + " does not exist.");
        }
        return ossService.getObjectUrl(configuration.getBucket(), waylineOpt.get().getObjectKey());
    }

    @Override
    public Integer saveWaylineFile(String workspaceId, WaylineFileDTO metadata) {
        WaylineFileEntity file = this.dtoConvertToEntity(metadata);
        file.setWaylineId(UUID.randomUUID().toString());
        file.setWorkspaceId(workspaceId);

        byte[] object = ossService.getObject(configuration.getBucket(), metadata.getObjectKey());
        if (object.length == 0) {
            throw new RuntimeException("The file " + metadata.getObjectKey() +
                    " does not exist in the bucket[" + configuration.getBucket() + "].");
        }

        file.setSign(DigestUtils.md5DigestAsHex(object));
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

    @Override
    public Boolean deleteByWaylineId(String workspaceId, String waylineId) {
        Optional<WaylineFileDTO> waylineOpt = this.getWaylineByWaylineId(workspaceId, waylineId);
        if (waylineOpt.isEmpty()) {
            return true;
        }
        WaylineFileDTO wayline = waylineOpt.get();
        boolean isDel = mapper.delete(new LambdaUpdateWrapper<WaylineFileEntity>()
                    .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                    .eq(WaylineFileEntity::getWaylineId, waylineId))
                > 0;
        if (!isDel) {
            return false;
        }
        return ossService.deleteObject(configuration.getBucket(), wayline.getObjectKey());
    }

    /**
     * Convert database entity objects into wayline data transfer object.
     * @param entity
     * @return
     */
    private WaylineFileDTO entityConvertToDTO(WaylineFileEntity entity) {
        if (entity == null) {
            return null;
        }
        return WaylineFileDTO.builder()
                .droneModelKey(entity.getDroneModelKey())
                .favorited(entity.getFavorited())
                .name(entity.getName())
                .payloadModelKeys(entity.getPayloadModelKeys() != null ?
                        Arrays.asList(entity.getPayloadModelKeys().split(",")) : null)
                .templateTypes(Arrays.stream(entity.getTemplateTypes().split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .username(entity.getUsername())
                .objectKey(entity.getObjectKey())
                .sign(entity.getSign())
                .updateTime(entity.getUpdateTime())
                .waylineId(entity.getWaylineId())
                .build();

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
