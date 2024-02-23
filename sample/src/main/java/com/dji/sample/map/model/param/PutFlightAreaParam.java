package com.dji.sample.map.model.param;

import com.dji.sample.map.model.dto.FlightAreaContent;
import lombok.Data;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Data
public class PutFlightAreaParam {

    private String name;

    private FlightAreaContent content;

    private Boolean status;

}
