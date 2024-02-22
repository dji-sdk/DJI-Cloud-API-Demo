package com.dji.sdk.cloudapi.log;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class FileUploadListRequest extends BaseModel {

    /**
     * Filter list of file
     **/
    @NotNull
    @Size(min = 1, max = 2)
    private List<LogModuleEnum> moduleList;

    public FileUploadListRequest() {
    }

    @Override
    public String toString() {
        return "FileUploadListRequest{" +
                "moduleList=" + moduleList +
                '}';
    }

    public List<LogModuleEnum> getModuleList() {
        return moduleList;
    }

    public FileUploadListRequest setModuleList(List<LogModuleEnum> moduleList) {
        this.moduleList = moduleList;
        return this;
    }
}
