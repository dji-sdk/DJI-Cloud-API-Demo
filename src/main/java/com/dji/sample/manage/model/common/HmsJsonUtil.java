package com.dji.sample.manage.model.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
@Slf4j
@Component
public class HmsJsonUtil {

    private static ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        HmsJsonUtil.mapper = mapper;
    }

    private static JsonNode nodes;

    private HmsJsonUtil(){

    }

    @PostConstruct
    private void loadJsonFile() {
        try (InputStream inputStream = new ClassPathResource("hms.json").getInputStream()){
            nodes = mapper.readTree(inputStream);
        } catch (IOException e) {
            log.error("hms.json failed to load.");
            e.printStackTrace();
        }
    }

    public static HmsMessage get(String key) {
        if (nodes.get(key) == null) {
            return new HmsMessage();
        }
        return mapper.convertValue(nodes.get(key), HmsMessage.class);
    }
}
