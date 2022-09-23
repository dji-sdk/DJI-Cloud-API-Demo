package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogsOutputProgressDTO {

    private String logsId;

    private String status;

    private List<LogsProgressDTO> files;
}
