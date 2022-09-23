package com.dji.sample.configuration.mvc;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        for (PropertyValue property : values) {
            String name = convertSnake(property.getName());
            if (!property.getName().equals(name)) {
                mpvs.addPropertyValue(new PropertyValue(name, property.getValue()));
                propertyValueList.remove(property);
            }
        }
        super.addBindValues(mpvs, request);
    }

    private String convertSnake(String key) {
        StringBuilder sb = new StringBuilder();
        boolean isChange = false;
        for (char c : key.toCharArray()) {
            if (c == '_') {
                isChange = true;
                continue;
            }
            if (isChange) {
                sb.append((char) (c - 32));
                isChange = false;
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
