package com.dji.sample.wayline.model.param;

import com.dji.sample.wayline.model.enums.WaylineTaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobParam {

    private WaylineTaskStatusEnum status;

}
