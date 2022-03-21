package com.dji.sample.manage.model.enums;

/**
 * The system icon that comes with the pilot.
 * @author sean
 * @version 0.3
 * @date 2022/1/5
 */
public enum IconUrlEnum {

    SELECT_CAR("resource://pilot/drawable/tsa_car_select"),

    NORMAL_CAR("resource://pilot/drawable/tsa_car_normal"),

    SELECT_PERSON("resource://pilot/drawable/tsa_person_select"),

    NORMAL_PERSON("resource://pilot/drawable/tsa_person_normal"),

    SELECT_EQUIPMENT("resource://pilot/drawable/tsa_equipment_select"),

    NORMAL_EQUIPMENT("resource://pilot/drawable/tsa_equipment_normal");

    /**
     * You can use icons from the web, and the App internally downloads and caches these icons and
     * loads them at a fixed size (28dp) to display on the map.
     * Example: http://r56978dr7.hn-bkt.clouddn.com/tsa_equipment_normal.png
     */
    private String url;

    IconUrlEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
