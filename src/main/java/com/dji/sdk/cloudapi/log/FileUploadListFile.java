package com.dji.sdk.cloudapi.log;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public class FileUploadListFile {

    private String deviceSn;

    private List<LogFileIndex> list;

    private LogModuleEnum module;

    private Integer result;

    public FileUploadListFile() {
    }

    @Override
    public String toString() {
        return "FileUploadListFile{" +
                "deviceSn='" + deviceSn + '\'' +
                ", list=" + list +
                ", module=" + module +
                ", result=" + result +
                '}';
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public FileUploadListFile setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
        return this;
    }

    public List<LogFileIndex> getList() {
        return list;
    }

    public FileUploadListFile setList(List<LogFileIndex> list) {
        this.list = list;
        return this;
    }

    public LogModuleEnum getModule() {
        return module;
    }

    public FileUploadListFile setModule(LogModuleEnum module) {
        this.module = module;
        return this;
    }

    public Integer getResult() {
        return result;
    }

    public FileUploadListFile setResult(Integer result) {
        this.result = result;
        return this;
    }
}
