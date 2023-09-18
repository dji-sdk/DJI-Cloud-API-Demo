package com.dji.sample.manage.model.dto;

import com.dji.sdk.cloudapi.tsa.TopologyList;
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

    private TopologyList deviceTopo;

    private List<LogsProgressDTO> logsProgress;

    private LogsFileUploadListDTO deviceLogs;

}
