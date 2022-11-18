package com.dji.sample.component.mqtt.model;

import com.dji.sample.manage.service.IRequestsConfigService;
import com.dji.sample.manage.service.impl.ConfigProductServiceImpl;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Getter
public enum ConfigScopeEnum {

    PRODUCT("product", ConfigProductServiceImpl.class);

    String scope;

    Class<? extends IRequestsConfigService> clazz;

    ConfigScopeEnum(String scope, Class<? extends IRequestsConfigService> clazz) {
        this.scope = scope;
        this.clazz = clazz;
    }

    public static Optional<ConfigScopeEnum> find(String scope) {
        return Arrays.stream(ConfigScopeEnum.values()).filter(scopeEnum -> scopeEnum.scope.equals(scope)).findAny();
    }
}
