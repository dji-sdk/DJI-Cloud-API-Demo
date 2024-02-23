package com.dji.sdk.annotations;

import com.dji.sdk.config.version.CloudSDKVersionEnum;
import com.dji.sdk.config.version.GatewayTypeEnum;

import java.lang.annotation.*;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CloudSDKVersion {

    CloudSDKVersionEnum since() default CloudSDKVersionEnum.V0_0_1;

    CloudSDKVersionEnum deprecated() default CloudSDKVersionEnum.V99;

    GatewayTypeEnum[] include() default {};

    GatewayTypeEnum[] exclude() default {};

}
