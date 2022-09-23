package com.dji.sample.manage.model.receiver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogsFile {

    private Integer bootIndex;

    private Long endTime;

    private Long startTime;

    private Long size;
}
