package com.dji.sample.manage.model.dto;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/1
 */
public class FirmwareFileProperties {

    private FirmwareFileProperties() {

    }

    public static final String FIRMWARE_FILE_SUFFIX = ".zip";

    public static final String FIRMWARE_SIG_FILE_SUFFIX = ".sig";

    public static final String FIRMWARE_CONFIG_FILE_SUFFIX = ".cfg";

    public static final String FIRMWARE_FILE_DELIMITER = "_";

    public static final int FILENAME_VERSION_INDEX = 2;

    public static final int FILENAME_RELEASE_DATE_INDEX = 3;

    public static final String FILENAME_RELEASE_DATE_FORMAT = "yyyyMMdd";

}
