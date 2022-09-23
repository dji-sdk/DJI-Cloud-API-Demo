package com.dji.sample.manage.model.dto;

import com.dji.sample.manage.model.receiver.LogsFileUploadList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLogsDTO {

    private String logsId;

    private LocalDateTime happenTime;

    private String userName;

    private String logsInformation;

    private LocalDateTime createTime;

    private Integer status;

    private TopologyDTO deviceTopo;

    private List<LogsProgressDTO> logsProgress;

    private LogsFileUploadList deviceLogs;

}
