package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.LinkWorkModeEnum;
import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkWorkMode extends BasicDeviceProperty {

    @JsonProperty("link_workmode")
    private Integer linkWorkMode;

    @Override
    public boolean valid() {
        return Objects.nonNull(linkWorkMode) && LinkWorkModeEnum.find(linkWorkMode).isPresent();
    }
}
