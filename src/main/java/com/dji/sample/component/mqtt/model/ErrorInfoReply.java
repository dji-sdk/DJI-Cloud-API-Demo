package com.dji.sample.component.mqtt.model;

import com.dji.sample.common.model.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/14
 */
@Data
@AllArgsConstructor
public class ErrorInfoReply {

    private String sn;

    private Integer errCode;

    public static ErrorInfoReply success(String sn) {
        return new ErrorInfoReply(sn, ResponseResult.CODE_SUCCESS);
    }
}
