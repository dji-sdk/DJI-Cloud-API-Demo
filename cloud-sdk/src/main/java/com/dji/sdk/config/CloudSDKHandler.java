package com.dji.sdk.config;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.common.*;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/22
 */
@Aspect
@Component
public class CloudSDKHandler {

    @Before("execution(public * com.dji.sdk.cloudapi.*.api.*.*(com.dji.sdk.config.version.GatewayManager, ..))")
    public void checkCloudSDK(JoinPoint point) {
        GatewayManager deviceSDK = (GatewayManager) point.getArgs()[0];
        CloudSDKVersion since = ((MethodSignature) point.getSignature()).getMethod().getDeclaredAnnotation(CloudSDKVersion.class);
        if (Objects.isNull(since)) {
            return;
        }
        if (!deviceSDK.isTypeSupport(since)) {
            throw new CloudSDKException(CloudSDKErrorEnum.DEVICE_TYPE_NOT_SUPPORT);
        }
        if (!deviceSDK.isVersionSupport(since)) {
            throw new CloudSDKException(CloudSDKErrorEnum.DEVICE_VERSION_NOT_SUPPORT);
        }
    }

    @Before("execution(public * com.dji.sdk.cloudapi.*.api.*.*(com.dji.sdk.config.version.GatewayManager, com.dji.sdk.common.BaseModel+))")
    public void checkRequest(JoinPoint point) {
        Common.validateModel((BaseModel) point.getArgs()[1], (GatewayManager) point.getArgs()[0]);
    }

    @AfterReturning(value = "execution(public com.dji.sdk.common.HttpResultResponse+ com.dji.sdk.cloudapi.*.api.*.*(..))", returning = "response")
    public void checkResponse(JoinPoint point, HttpResultResponse response) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (null == response) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, "The return value cannot be null.");
        }
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        if (method.getGenericReturnType() instanceof Class) {
            if (null == response.getData()) {
                response.setData("");
            }
            return;
        }
        checkClassType((ParameterizedType) method.getGenericReturnType(), response);
        validData(response.getData(), point.getArgs()[0]);
    }

    private void checkClassType(ParameterizedType type, HttpResultResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Type actualType = type.getActualTypeArguments()[0];
        Class typeClass = actualType instanceof Class ? (Class) actualType : (Class) ((ParameterizedType) actualType).getRawType();
        if (null == response.getData()) {
            if (List.class.isAssignableFrom(typeClass)) {
                response.setData(Collections.emptyList());
                return;
            }
            response.setData(typeClass.getDeclaredConstructor().newInstance());
            return;
        }
        boolean isAssignableFrom = typeClass.isAssignableFrom(response.getData().getClass());
        if (!isAssignableFrom) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
    }

    private void validData(Object data, Object arg) {
        if (data instanceof BaseModel) {
            Common.validateModel((BaseModel) data);
            return;
        }
        if (data instanceof PaginationData) {
            List<BaseModel> list = ((PaginationData) data).getList();
            if (null == list) {
                ((PaginationData) data).setList(Collections.EMPTY_LIST);
                try {
                    Field page = arg.getClass().getDeclaredField("page");
                    Field pageSize = arg.getClass().getDeclaredField("pageSize");
                    page.setAccessible(true);
                    pageSize.setAccessible(true);
                    ((PaginationData) data).setPagination(
                            new Pagination().setPage((int) page.get(arg)).setPageSize((int) pageSize.get(arg)));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, e.getMessage());
                }
                return;
            }
            for (BaseModel model : list) {
                Common.validateModel(model);
            }
        }
    }
}
