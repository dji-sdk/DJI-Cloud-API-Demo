package com.dji.sample.control.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultNotifyDTO {

    private Integer result;

    private String message;

    private String sn;
}
