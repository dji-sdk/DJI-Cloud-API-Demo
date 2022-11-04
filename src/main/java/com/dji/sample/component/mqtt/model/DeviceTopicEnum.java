package com.dji.sample.component.mqtt.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
@Getter
public enum DeviceTopicEnum {

    STATUS(Pattern.compile("^" + BASIC_PRE + PRODUCT + REGEX_SN + STATUS_SUF + "$"), ChannelName.INBOUND_STATUS),

    STATE(Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + STATE_SUF + "$"), ChannelName.INBOUND_STATE),

    SERVICE_REPLY(Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + SERVICES_SUF + _REPLY_SUF + "$"), ChannelName.INBOUND_SERVICE_REPLY),

    OSD(Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + OSD_SUF + "$"), ChannelName.INBOUND_OSD),

    REQUESTS(Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + REQUESTS_SUF + "$"), ChannelName.INBOUND_REQUESTS),

    EVENTS(Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + EVENTS_SUF + "$"), ChannelName.INBOUND_EVENTS),

    PROPERTY_SET_REPLY(Pattern.compile("^" + THING_MODEL_PRE + PRODUCT + REGEX_SN + PROPERTY_SUF + SET_SUF + _REPLY_SUF + "$"), ChannelName.INBOUND_PROPERTY_SET_REPLY),

    UNKNOWN(null, ChannelName.DEFAULT);

    Pattern pattern;

    String beanName;

    DeviceTopicEnum(Pattern pattern, String beanName) {
        this.pattern = pattern;
        this.beanName = beanName;
    }

    public static DeviceTopicEnum find(String topic) {
        return Arrays.stream(DeviceTopicEnum.values()).filter(topicEnum -> topicEnum.pattern.matcher(topic).matches()).findAny().orElse(UNKNOWN);
    }
}
