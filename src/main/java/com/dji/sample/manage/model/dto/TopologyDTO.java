package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopologyDTO {

    private List<TopologyDeviceDTO> hosts;

    private List<TopologyDeviceDTO> parents;
}
