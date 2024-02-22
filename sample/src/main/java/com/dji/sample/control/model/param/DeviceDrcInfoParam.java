package com.dji.sample.control.model.param;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/2
 */
@Data
public class DeviceDrcInfoParam {

    @Range(min = 1, max = 30)
    private Integer osdFrequency = 10;

    @Range(min = 1, max = 30)
    private Integer hsiFrequency = 1;
}
