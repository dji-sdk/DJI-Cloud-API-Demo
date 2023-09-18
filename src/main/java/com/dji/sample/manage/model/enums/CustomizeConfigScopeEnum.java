package com.dji.sample.manage.model.enums;

import com.dji.sample.manage.service.IRequestsConfigService;
import com.dji.sample.manage.service.impl.ConfigProductServiceImpl;
import com.dji.sdk.cloudapi.config.ConfigScopeEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Getter
public enum CustomizeConfigScopeEnum {

    PRODUCT(ConfigScopeEnum.PRODUCT, ConfigProductServiceImpl.class);

    ConfigScopeEnum scope;

    Class<? extends IRequestsConfigService> clazz;

    CustomizeConfigScopeEnum(ConfigScopeEnum scope, Class<? extends IRequestsConfigService> clazz) {
        this.scope = scope;
        this.clazz = clazz;
    }

    public static Optional<CustomizeConfigScopeEnum> find(String scope) {
        return Arrays.stream(CustomizeConfigScopeEnum.values()).filter(scopeEnum -> scopeEnum.scope.getScope().equals(scope)).findAny();
    }
}
