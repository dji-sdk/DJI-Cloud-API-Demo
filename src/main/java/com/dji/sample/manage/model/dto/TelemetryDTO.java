package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryDTO<T> {

    private T host;

    private String sn;
}
