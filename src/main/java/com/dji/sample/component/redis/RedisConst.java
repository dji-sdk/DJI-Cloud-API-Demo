package com.dji.sample.component.redis;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/21
 */
public final class RedisConst {

    public static final int WAYLINE_JOB_BLOCK_TIME = 600;

    private RedisConst() {

    }

    public static final String DELIMITER = ":";

    public static final Integer DEVICE_ALIVE_SECOND = 60;

    public static final Integer WEBSOCKET_ALIVE_SECOND = 60 * 60 * 24;

    public static final String DEVICE_ONLINE_PREFIX = "online" + DELIMITER;

    public static final String WEBSOCKET_PREFIX = "webSocket" + DELIMITER;

    public static final String WEBSOCKET_ALL = WEBSOCKET_PREFIX + "all";

    public static final String HMS_PREFIX = "hms" + DELIMITER;

    public static final String FIRMWARE_UPGRADING_PREFIX = "upgrading" + DELIMITER;

    public static final String STATE_PAYLOAD_PREFIX = "payload" + DELIMITER;

    public static final String LOGS_FILE_PREFIX = "logs_file" + DELIMITER;

    public static final String WAYLINE_JOB_TIMED_EXECUTE = "wayline_job_timed_execute";

    public static final String WAYLINE_JOB_CONDITION_PREPARE = "wayline_job_condition_prepare";

    public static final String WAYLINE_JOB_CONDITION_PREFIX = WAYLINE_JOB_CONDITION_PREPARE + DELIMITER;

    public static final String WAYLINE_JOB_BLOCK_PREFIX = "wayline_job_block" + DELIMITER;

    public static final String WAYLINE_JOB_RUNNING_PREFIX = "wayline_job_running" + DELIMITER;

    public static final String WAYLINE_JOB_PAUSED_PREFIX = "wayline_job_paused" + DELIMITER;

    public static final String OSD_PREFIX = "osd" + DELIMITER;

    public static final String MEDIA_FILE_PREFIX = "media_file" + DELIMITER;

    public static final String MEDIA_HIGHEST_PRIORITY_PREFIX = "media_highest_priority" + DELIMITER;

    public static final String LIVE_CAPACITY = "live_capacity";

    public static final String DRC_PREFIX = "drc" + DELIMITER;

    public static final Integer DRC_MODE_ALIVE_SECOND = 3600;

    public static final String MQTT_ACL_PREFIX = "mqtt_acl" + DELIMITER;

    public static final String FILE_UPLOADING_PREFIX = "file_uploading" + DELIMITER;

    public static final String DRONE_CONTROL_PREFiX = "control_source" + DELIMITER;
}