package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/14
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class BindStatusReceiver {

    private String sn;

    private Boolean isDeviceBindOrganization;

    private String organizationId;

    private String organizationName;

    private String deviceCallsign;
}
