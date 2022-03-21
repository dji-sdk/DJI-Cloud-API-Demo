package com.dji.sample.manage.model.param;

import lombok.Builder;
import lombok.Data;

/**
 * The object of the device query field.
 *
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Data
@Builder
public class DeviceQueryParam {

    private String deviceSn;

    private String workspaceId;

    private Integer deviceType;

    private Integer subType;

    private Integer domain;

    private String childSn;

    private boolean orderBy;

    private boolean isAsc;
}