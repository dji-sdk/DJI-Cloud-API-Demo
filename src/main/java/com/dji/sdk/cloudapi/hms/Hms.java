package com.dji.sdk.cloudapi.hms;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public class Hms {

    private List<DeviceHms> list;

    public Hms() {
    }

    @Override
    public String toString() {
        return "Hms{" +
                "list=" + list +
                '}';
    }

    public List<DeviceHms> getList() {
        return list;
    }

    public Hms setList(List<DeviceHms> list) {
        this.list = list;
        return this;
    }
}
