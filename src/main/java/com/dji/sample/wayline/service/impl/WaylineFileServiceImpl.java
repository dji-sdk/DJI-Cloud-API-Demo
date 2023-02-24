package com.dji.sample.wayline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.wayline.dao.IWaylineFileMapper;
import com.dji.sample.wayline.model.dto.KmzFileProperties;
import com.dji.sample.wayline.model.dto.WaylineFileDTO;
import com.dji.sample.wayline.model.entity.WaylineFileEntity;
import com.dji.sample.wayline.model.param.WaylineQueryParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.dji.sample.wayline.model.dto.KmzFileProperties.WAYLINE_FILE_SUFFIX;

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
        return ossService.getObjectUrl(OssConfiguration.bucket, waylineOpt.get().getObjectKey());
    }

    @Override
    public Integer saveWaylineFile(String workspaceId, WaylineFileDTO metadata) {
        WaylineFileEntity file = this.dtoConvertToEntity(metadata);
        file.setWaylineId(UUID.randomUUID().toString());
        file.setWorkspaceId(workspaceId);

        if (!StringUtils.hasText(file.getSign())) {
            try (InputStream object = ossService.getObject(OssConfiguration.bucket, metadata.getObjectKey())) {
                if (object.available() == 0) {
                    throw new RuntimeException("The file " + metadata.getObjectKey() +
                            " does not exist in the bucket[" + OssConfiguration.bucket + "].");
                }
                file.setSign(DigestUtils.md5DigestAsHex(object));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        return ossService.deleteObject(OssConfiguration.bucket, wayline.getObjectKey());
    }

    @Override
    public void importKmzFile(MultipartFile file, String workspaceId, String creator) {
        Optional<WaylineFileDTO> waylineFileOpt = validKmzFile(file);
        if (waylineFileOpt.isEmpty()) {
            throw new RuntimeException("The file format is incorrect.");
        }

        try {
            WaylineFileDTO waylineFile = waylineFileOpt.get();
            waylineFile.setWaylineId(workspaceId);
            waylineFile.setUsername(creator);

            ossService.putObject(OssConfiguration.bucket, waylineFile.getObjectKey(), file.getInputStream());
            this.saveWaylineFile(workspaceId, waylineFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<WaylineFileDTO> validKmzFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (Objects.nonNull(filename) && !filename.endsWith(WAYLINE_FILE_SUFFIX)) {
            throw new RuntimeException("The file format is incorrect.");
        }
        try (ZipInputStream unzipFile = new ZipInputStream(file.getInputStream(), StandardCharsets.UTF_8)) {

            ZipEntry nextEntry = unzipFile.getNextEntry();
            while (Objects.nonNull(nextEntry)) {
                boolean isWaylines = (KmzFileProperties.FILE_DIR_FIRST + "/" + KmzFileProperties.FILE_DIR_SECOND_WAYLINES).equals(nextEntry.getName());
                if (!isWaylines) {
                    nextEntry = unzipFile.getNextEntry();
                    continue;
                }
                SAXReader reader = new SAXReader();
                Document document = reader.read(unzipFile);
                if (!StandardCharsets.UTF_8.name().equals(document.getXMLEncoding())) {
                    throw new RuntimeException("The file encoding format is incorrect.");
                }

                Node droneNode = document.selectSingleNode("//" + KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_DRONE_INFO);
                Node payloadNode = document.selectSingleNode("//" + KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_PAYLOAD_INFO);
                if (Objects.isNull(droneNode) || Objects.isNull(payloadNode)) {
                    throw new RuntimeException("The file format is incorrect.");
                }

                String type = droneNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_DRONE_ENUM_VALUE);
                String subType = droneNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_DRONE_SUB_ENUM_VALUE);
                String payloadType = payloadNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_PAYLOAD_ENUM_VALUE);
                String payloadSubType = payloadNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_PAYLOAD_SUB_ENUM_VALUE);
                String templateId = document.valueOf("//" + KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_TEMPLATE_ID);

                if (!StringUtils.hasText(type) || !StringUtils.hasText(subType) ||
                        !StringUtils.hasText(payloadSubType) || !StringUtils.hasText(payloadType) ||
                        !StringUtils.hasText(templateId)) {
                    throw new RuntimeException("The file format is incorrect.");
                }

                return Optional.of(WaylineFileDTO.builder()
                        .droneModelKey(String.format("%s-%s-%s", DeviceDomainEnum.SUB_DEVICE.getVal(), type, subType))
                        .payloadModelKeys(List.of(String.format("%s-%s-%s",DeviceDomainEnum.PAYLOAD.getVal(), payloadType, payloadSubType)))
                        .objectKey(OssConfiguration.objectDirPrefix + File.separator + filename)
                        .name(filename.substring(0, filename.lastIndexOf(WAYLINE_FILE_SUFFIX)))
                        .sign(DigestUtils.md5DigestAsHex(file.getInputStream()))
                        .templateTypes(List.of(Integer.parseInt(templateId)))
                        .build());
            }

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return Optional.empty();
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
                    .sign(file.getSign())
                    .build();
        }

        return builder.build();
    }
}
