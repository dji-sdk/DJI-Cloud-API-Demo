package com.dji.sdk.config;

import com.dji.sdk.common.Common;
import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
public class GetSnakeDataBinder extends ExtendedServletRequestDataBinder {

    public GetSnakeDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        List<PropertyValue> propertyValueList = mpvs.getPropertyValueList();
        List<PropertyValue> values = new ArrayList<>(propertyValueList);
        Field[] fields = this.getTarget().getClass().getDeclaredFields();
        Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, field -> field));
        fieldMap.putAll(Arrays.stream(fields).filter(field ->  null != field.getAnnotation(JsonProperty.class))
                .collect(Collectors.toMap(field -> field.getAnnotation(JsonProperty.class).value(), field -> field)));

        for (PropertyValue property : values) {
            if (!fieldMap.containsKey(property.getName())) {
                continue;
            }

            Field field = fieldMap.get(property.getName());
            List list = (List) Objects.requireNonNullElse(property.getConvertedValue(), new ArrayList<>());
            list.addAll((List) convertValue(field, this.getTarget(), property.getValue()));
            property.setConvertedValue(list);

            String fieldName = field.getName();
            if (mpvs.contains(fieldName)) {
                PropertyValue propertyValue = mpvs.getPropertyValue(fieldName);
                if (propertyValue != property && null != propertyValue.getConvertedValue()) {
                    ((List) propertyValue.getConvertedValue()).addAll((List) property.getConvertedValue());
                    property = propertyValue;
                }
            }
            Object data = Collection.class.isAssignableFrom(field.getType()) ? property.getConvertedValue() : ((List) property.getConvertedValue()).get(0);
            mpvs.addPropertyValue(new PropertyValue(fieldName, Objects.requireNonNullElse(data, property.getValue())));
        }

        super.addBindValues(mpvs, request);
    }

    private Object convertValue(Field field, Object object, Object value) {
        List convertedValue = new ArrayList();
        if (Enum.class.isAssignableFrom(field.getType())) {
            convertedValue.add(getRealEnumValue(field.getType(), object, value));
            return convertedValue;
        }
        if (field.getType() == field.getGenericType()) {
            convertedValue.add(value);
            return convertedValue;
        }
        if (Collection.class.isAssignableFrom(field.getType())) {
            if (!value.getClass().isArray()) {
                value = String.valueOf(value).split(",");
            }
            for (String v : (String[]) value) {
                if ("".equals(v)) {
                    continue;
                }
                convertedValue.add(getRealEnumValue((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0], object, v));
            }
        }

        return convertedValue;
    }

    private Object getRealEnumValue(Class type, Object object, Object... value) {
        if (!type.isEnum()) {
            return value;
        }
        Set<Method> methods = Arrays.stream(type.getDeclaredMethods())
                .filter(m -> null != m.getAnnotation(JsonCreator.class))
                .filter(m -> m.getParameterTypes().length == value.length).collect(Collectors.toSet());
        for (Method m : methods) {
            try {
                Class<?>[] parameterTypes = m.getParameterTypes();
                for (int i = 0; i < value.length; i++) {
                    value[i] = Common.getObjectMapper().convertValue(value[i], parameterTypes[i]);
                }
                return m.invoke(object, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CloudSDKException(e);
            }
        }
        return value;
    }
}
