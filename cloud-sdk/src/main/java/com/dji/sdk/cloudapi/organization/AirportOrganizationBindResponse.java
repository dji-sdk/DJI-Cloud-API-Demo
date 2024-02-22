package com.dji.sdk.cloudapi.organization;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class AirportOrganizationBindResponse extends BaseModel {

    @NotNull
    @Size(min = 1, max = 2)
    private List<@Valid OrganizationBindInfo> errInfos;

    public AirportOrganizationBindResponse() {
    }

    @Override
    public String toString() {
        return "AirportOrganizationBindResponse{" +
                "errInfos=" + errInfos +
                '}';
    }

    public List<OrganizationBindInfo> getErrInfos() {
        return errInfos;
    }

    public AirportOrganizationBindResponse setErrInfos(List<OrganizationBindInfo> errInfos) {
        this.errInfos = errInfos;
        return this;
    }
}
