package com.dji.sample.wayline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaylineJobDTO {

    private String jobId;

    private String jobName;

    private String fileId;

    private String fileName;

    private String dockSn;

    private String dockName;

    private String workspaceId;

    private String bid;

    private String type;

    private String username;

    private LocalDateTime updateTime;

}
