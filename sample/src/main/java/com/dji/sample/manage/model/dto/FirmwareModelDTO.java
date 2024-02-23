package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirmwareModelDTO {

    private String firmwareId;

    private List<String> deviceNames;
}
