package com.dji.sample.manage.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
public class LogsFileUpdateParam {

    private String status;

    @JsonProperty("module_list")
    private List<String> deviceModelDomainList;

}
