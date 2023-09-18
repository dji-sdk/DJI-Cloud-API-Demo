package com.dji.sample.manage.controller;

import com.dji.sample.manage.service.ITopologyService;
import com.dji.sdk.cloudapi.tsa.TopologyList;
import com.dji.sdk.cloudapi.tsa.TopologyResponse;
import com.dji.sdk.cloudapi.tsa.api.IHttpTsaService;
import com.dji.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@RestController
public class TopologyController implements IHttpTsaService {

    @Autowired
    private ITopologyService topologyService;


    /**
     * Get the topology list of all devices in the current user workspace for pilot display.
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse<TopologyResponse> obtainDeviceTopologyList(String workspaceId, HttpServletRequest req, HttpServletResponse rsp) {
        List<TopologyList> topologyList = topologyService.getDeviceTopology(workspaceId);
        return HttpResultResponse.success(new TopologyResponse().setList(topologyList));
    }
}
