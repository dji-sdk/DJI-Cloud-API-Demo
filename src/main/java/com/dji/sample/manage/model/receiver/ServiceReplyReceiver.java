package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceReplyReceiver<T> {

    private Integer result;

    private String info;
}