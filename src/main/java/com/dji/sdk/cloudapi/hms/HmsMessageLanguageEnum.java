package com.dji.sdk.cloudapi.hms;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
public enum HmsMessageLanguageEnum {

    EN("en"),

    ZH("zh");

    private final String language;

    HmsMessageLanguageEnum(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}