package com.dji.sdk.cloudapi.log;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public class FileUploadStartFile {

    @NotNull
    private String deviceSn;

    @NotNull
    private List<@Valid LogFileIndex> list;

    @NotNull
    private LogModuleEnum module;

    @NotNull
    private String objectKey;

    public FileUploadStartFile() {
    }

    @Override
    public String toString() {
        return "FileUploadStartFile{" +
                "deviceSn='" + deviceSn + '\'' +
                ", list=" + list +
                ", module=" + module +
                ", objectKey='" + objectKey + '\'' +
                '}';
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public FileUploadStartFile setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
        return this;
    }

    public List<LogFileIndex> getList() {
        return list;
    }

    public FileUploadStartFile setList(List<LogFileIndex> list) {
        this.list = list;
        return this;
    }

    public LogModuleEnum getModule() {
        return module;
    }

    public FileUploadStartFile setModule(LogModuleEnum module) {
        this.module = module;
        return this;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public FileUploadStartFile setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }
}
