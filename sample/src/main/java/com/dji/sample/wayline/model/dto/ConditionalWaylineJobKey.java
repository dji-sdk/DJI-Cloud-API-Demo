package com.dji.sample.wayline.model.dto;

import com.dji.sample.component.redis.RedisConst;
import lombok.Data;

import java.util.Objects;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/28
 */
@Data
public class ConditionalWaylineJobKey {

    private String workspaceId;

    private String dockSn;

    private String jobId;

    public ConditionalWaylineJobKey(String workspaceId, String dockSn, String jobId) {
        this.workspaceId = workspaceId;
        this.dockSn = dockSn;
        this.jobId = jobId;
    }

    public ConditionalWaylineJobKey(String key) {
        if (Objects.isNull(key)) {
            return;
        }
        String[] keyArr = key.split(RedisConst.DELIMITER);
        new ConditionalWaylineJobKey(keyArr[0], keyArr[1], keyArr[2]);
    }

    public String getKey() {
        return String.join(RedisConst.DELIMITER, workspaceId, dockSn, jobId);
    }
}
