package com.dji.sdk.common;

import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class Common {

    private static final JsonMapper.Builder MAPPER_BUILDER = JsonMapper.builder();

    static {
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        MAPPER_BUILDER.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .serializationInclusion(JsonInclude.Include.NON_ABSENT)
                .disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS)
                .addModule(timeModule)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static void validateModel(BaseModel model) {
        if (null == model) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, "Param must not be null.");
        }
        model.valid();
    }

    public static void validateModel(BaseModel model, GatewayManager gateway) {
        if (null == model) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, "Param must not be null.");
        }
        model.valid(gateway);
    }

    public static ObjectMapper getObjectMapper() {
        return MAPPER_BUILDER.build();
    }

    public static String convertSnake(String key) {
        StringBuilder sb = new StringBuilder();
        boolean isChange = false;
        for (char c : key.toCharArray()) {
            if (c == '_') {
                isChange = true;
                continue;
            }
            if (isChange) {
                sb.append((char)(c - 32));
                isChange = false;
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
