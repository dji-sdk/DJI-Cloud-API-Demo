package com.dji.sample.media.service.impl;

import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.media.model.MediaFileCountDTO;
import com.dji.sample.media.service.IMediaRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Service
@Slf4j
public class MediaRedisServiceImpl implements IMediaRedisService {


    @Override
    public void setMediaCount(String gatewaySn, String jobId, MediaFileCountDTO mediaFile) {
        RedisOpsUtils.hashSet(RedisConst.MEDIA_FILE_PREFIX + gatewaySn, jobId, mediaFile);
    }

    @Override
    public MediaFileCountDTO getMediaCount(String gatewaySn, String jobId) {
        return (MediaFileCountDTO) RedisOpsUtils.hashGet(RedisConst.MEDIA_FILE_PREFIX + gatewaySn, jobId);
    }

    @Override
    public boolean delMediaCount(String gatewaySn, String jobId) {
        return RedisOpsUtils.hashDel(RedisConst.MEDIA_FILE_PREFIX + gatewaySn, new String[]{jobId});
    }

    @Override
    public boolean detMediaCountByDeviceSn(String gatewaySn) {
        return RedisOpsUtils.del(RedisConst.MEDIA_FILE_PREFIX + gatewaySn);
    }

    @Override
    public void setMediaHighestPriority(String gatewaySn, MediaFileCountDTO mediaFile) {
        RedisOpsUtils.setWithExpire(RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + gatewaySn, mediaFile, RedisConst.DEVICE_ALIVE_SECOND * 5);
    }

    @Override
    public MediaFileCountDTO getMediaHighestPriority(String gatewaySn) {
        return (MediaFileCountDTO) RedisOpsUtils.get(RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + gatewaySn);
    }

    @Override
    public boolean delMediaHighestPriority(String gatewaySn) {
        return RedisOpsUtils.del(RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + gatewaySn);
    }
}
