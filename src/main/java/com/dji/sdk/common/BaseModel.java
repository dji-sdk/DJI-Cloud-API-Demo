package com.dji.sdk.common;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class BaseModel {

    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public void valid() {
        this.valid(null);
    }

    public void checkProperty(String fieldName, GatewayManager gateway) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            CloudSDKVersion annotation = field.getDeclaredAnnotation(CloudSDKVersion.class);
            if (!gateway.isTypeSupport(annotation) || !gateway.isVersionSupport(annotation)) {
                throw new CloudSDKException(CloudSDKErrorEnum.DEVICE_PROPERTY_NOT_SUPPORT, fieldName);
            }
        } catch (NoSuchFieldException e) {
            throw new CloudSDKException(e);
        }
    }

    public void valid(GatewayManager gateway) {
        Set<ConstraintViolation<BaseModel>> violations = VALIDATOR.validate(this);
        if (null != gateway) {
            Set<String> names = new HashSet<>();
            violations = violations.stream().filter(violation ->
                    filterProperty(gateway, violation.getRootBeanClass(),
                        violation.getPropertyPath().toString().split("\\."), 0, true, names))
                    .collect(Collectors.toSet());
        }

        if (CollectionUtils.isEmpty(violations)) {
            return;
        }
        throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, violations.stream()
                .map(violation -> violation.getPropertyPath().toString() + violation.getMessage() +
                        ", Current value is: " + violation.getInvalidValue())
                .collect(Collectors.joining("; ")));

    }

    private boolean filterProperty(GatewayManager gateway, Class clazz, String[] fields, int index, boolean isValid, Set<String> names) {
        if (!isValid || index == fields.length) {
            return isValid;
        }
        String name = String.join(".", Arrays.copyOf(fields, index + 1));
        if (names.contains(name)) {
            return false;
        }
        try {
            Field field = clazz.getDeclaredField(fields[index]);
            isValid = gateway.isPropertyValid(field.getAnnotation(CloudSDKVersion.class));
            if (!isValid) {
                names.add(name);
            }
            return filterProperty(gateway, field.getType(), fields, index + 1, isValid, names);
        } catch (NoSuchFieldException e) {
            throw new CloudSDKException(e);
        }
    }
}