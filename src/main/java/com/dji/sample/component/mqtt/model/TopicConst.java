package com.dji.sample.component.mqtt.model;

/**
 * All the topics that need to be used in the project.
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public class TopicConst {

    public static final String BASIC_PRE = "sys/";

    public static final String THING_MODEL_PRE = "thing/";

    public static final String PRODUCT = "product/";

    public static final String STATUS_SUF = "/status";

    public static final String _REPLY_SUF = "_reply";

    public static final String STATE_SUF = "/state";

    public static final String SERVICES_SUF = "/services";

    public static final String OSD_SUF = "/osd";

    public static final String REQUESTS_SUF = "/requests";

    public static final String EVENTS_SUF = "/events";

    public static final String REGEX_SN = "[A-Za-z0-9]+";

}