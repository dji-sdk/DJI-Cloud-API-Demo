package com.dji.sample.control.model.param;

import com.dji.sdk.cloudapi.control.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlyToPointParam {

    private String flyToId;

    @Range(min = 1, max = 15)
    @NotNull
    private Integer maxSpeed;

    /**
     * The M30 series only support one point.
     */
    @Size(min = 1)
    @NotNull
    private List<@Valid Point> points;
}
