package com.dji.sample.wayline.model.dto;

import com.dji.sample.component.redis.RedisConst;
import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/28
 */
@Data
public class WaylineJobKey {

    private String workspaceId;

    private String dockSn;

    private String jobId;

    public WaylineJobKey(String workspaceId, String dockSn, String jobId) {
        this.workspaceId = workspaceId;
        this.dockSn = dockSn;
        this.jobId = jobId;
    }
    private WaylineJobKey(String[] keyArr) {
        this(keyArr[0], keyArr[1], keyArr[2]);
    }

    public WaylineJobKey(String key) {
        this(key.split(RedisConst.DELIMITER));
    }

    public String getKey() {
        return String.join(RedisConst.DELIMITER, workspaceId, dockSn, jobId);
    }
}
