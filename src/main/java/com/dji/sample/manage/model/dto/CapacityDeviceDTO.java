package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapacityDeviceDTO {

    private String sn;

    private String name;

    private List<CapacityCameraDTO> camerasList;
}