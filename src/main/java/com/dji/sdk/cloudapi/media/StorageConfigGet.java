package com.dji.sdk.cloudapi.media;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public class StorageConfigGet {

    private StorageConfigGetModuleEnum module;

    public StorageConfigGet() {
    }

    @Override
    public String toString() {
        return "StorageConfigGet{" +
                "module=" + module +
                '}';
    }

    public StorageConfigGetModuleEnum getModule() {
        return module;
    }

    public StorageConfigGet setModule(StorageConfigGetModuleEnum module) {
        this.module = module;
        return this;
    }
}
