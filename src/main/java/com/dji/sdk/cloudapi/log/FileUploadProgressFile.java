package com.dji.sdk.cloudapi.log;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
public class FileUploadProgressFile {

    private LogModuleEnum module;

    private Long size;

    private String deviceSn;

    private String key;

    private String fingerprint;

    private LogFileProgress progress;

    public FileUploadProgressFile() {
    }

    @Override
    public String toString() {
        return "FileUploadProgressFile{" +
                "module=" + module +
                ", size=" + size +
                ", deviceSn='" + deviceSn + '\'' +
                ", key='" + key + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", progress=" + progress +
                '}';
    }

    public LogModuleEnum getModule() {
        return module;
    }

    public FileUploadProgressFile setModule(LogModuleEnum module) {
        this.module = module;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public FileUploadProgressFile setSize(Long size) {
        this.size = size;
        return this;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public FileUploadProgressFile setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
        return this;
    }

    public String getKey() {
        return key;
    }

    public FileUploadProgressFile setKey(String key) {
        this.key = key;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public FileUploadProgressFile setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public LogFileProgress getProgress() {
        return progress;
    }

    public FileUploadProgressFile setProgress(LogFileProgress progress) {
        this.progress = progress;
        return this;
    }
}
