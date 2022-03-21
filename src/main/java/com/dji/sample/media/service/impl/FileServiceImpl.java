package com.dji.sample.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.manage.model.dto.DeviceDictionaryDTO;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import com.dji.sample.media.dao.IFileMapper;
import com.dji.sample.media.model.FileUploadDTO;
import com.dji.sample.media.model.MediaFileDTO;
import com.dji.sample.media.model.MediaFileEntity;
import com.dji.sample.media.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Service
@Transactional
public class FileServiceImpl implements IFileService {


    @Autowired
    private IFileMapper mapper;

    @Autowired
    private IDeviceDictionaryService deviceDictionaryService;

    @Override
    public Boolean checkExist(String workspaceId, String fingerprint) {
        MediaFileEntity fileEntity = mapper.selectOne(new LambdaQueryWrapper<MediaFileEntity>()
                .eq(MediaFileEntity::getWorkspaceId, workspaceId)
                .eq(MediaFileEntity::getFingerprint, fingerprint));
        return fileEntity != null;
    }

    @Override
    public Integer saveFile(String workspaceId, FileUploadDTO file) {
        MediaFileEntity fileEntity = this.fileUploadConvertToEntity(file);
        fileEntity.setWorkspaceId(workspaceId);
        return mapper.insert(fileEntity);
    }

    @Override
    public List<MediaFileDTO> getAllFilesByWorkspaceId(String workspaceId) {
        return mapper.selectList(new LambdaQueryWrapper<MediaFileEntity>()
                .eq(MediaFileEntity::getWorkspaceId, workspaceId))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert the received file object into a database entity object.
     * @param file
     * @return
     */
    private MediaFileEntity fileUploadConvertToEntity(FileUploadDTO file) {
        MediaFileEntity.MediaFileEntityBuilder builder = MediaFileEntity.builder();

        if (file != null) {
            builder.fileName(file.getName())
                    .filePath(file.getPath())
                    .fingerprint(file.getFingerprint())
                    .objectKey(file.getObjectKey())
                    .subFileType(file.getSubFileType())
                    .isOriginal(file.getExt().getIsOriginal())
                    .drone(file.getExt().getSn())
                    .tinnyFingerprint(file.getExt().getTinnyFingerprint());

            // domain-type-subType
            int[] payloadModel = Arrays.stream(file.getExt().getPayloadModelKey().split("-"))
                    .map(Integer::valueOf)
                    .mapToInt(Integer::intValue)
                    .toArray();
            Optional<DeviceDictionaryDTO> payloadDict = deviceDictionaryService
                    .getOneDictionaryInfoByDomainTypeSubType(payloadModel[0], payloadModel[1], payloadModel[2]);
            payloadDict.ifPresent(payload -> builder.payload(payload.getDeviceName()));
        }
        return builder.build();
    }

    /**
     * Convert database entity objects into file data transfer object.
     * @param entity
     * @return
     */
    private MediaFileDTO entityConvertToDto(MediaFileEntity entity) {
        MediaFileDTO.MediaFileDTOBuilder builder = MediaFileDTO.builder();

        if (entity != null) {
            builder.fileName(entity.getFileName())
                    .filePath(entity.getFilePath())
                    .isOriginal(entity.getIsOriginal())
                    .objectKey(entity.getObjectKey())
                    .tinnyFingerprint(entity.getTinnyFingerprint())
                    .payload(entity.getPayload())
                    .drone(entity.getDrone());

        }

        return builder.build();
    }

}
