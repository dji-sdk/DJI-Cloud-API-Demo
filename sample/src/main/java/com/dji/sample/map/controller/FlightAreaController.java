package com.dji.sample.map.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.map.model.dto.FlightAreaDTO;
import com.dji.sample.map.model.param.PostFlightAreaParam;
import com.dji.sample.map.model.param.PutFlightAreaParam;
import com.dji.sample.map.model.param.SyncFlightAreaParam;
import com.dji.sample.map.service.IFlightAreaService;
import com.dji.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/21
 */
@RestController
@RequestMapping("${url.map.prefix}${url.map.version}/workspaces")
public class FlightAreaController {

    @Autowired
    private IFlightAreaService flightAreaService;

    @GetMapping("/{workspace_id}/flight-areas")
    public HttpResultResponse<List<FlightAreaDTO>> getFlightAreas(@PathVariable(name = "workspace_id") String workspaceId) {
        return HttpResultResponse.success(flightAreaService.getFlightAreaList(workspaceId));
    }

    @PostMapping("/{workspace_id}/flight-area")
    public HttpResultResponse createFlightArea(@PathVariable(name = "workspace_id") String workspaceId,
                                               @Valid @RequestBody PostFlightAreaParam param, HttpServletRequest req) {
        CustomClaim claims = (CustomClaim) req.getAttribute(TOKEN_CLAIM);
        flightAreaService.createFlightArea(workspaceId, claims.getUsername(), param);
        return HttpResultResponse.success();
    }

    @DeleteMapping("/{workspace_id}/flight-area/{area_id}")
    public HttpResultResponse deleteFlightArea(@PathVariable(name = "workspace_id") String workspaceId,
                                               @PathVariable(name = "area_id") String areaId) {
        flightAreaService.deleteFlightArea(workspaceId, areaId);
        return HttpResultResponse.success();
    }

    @PutMapping("/{workspace_id}/flight-area/{area_id}")
    public HttpResultResponse updateFlightArea(@PathVariable(name = "workspace_id") String workspaceId,
                                               @PathVariable(name = "area_id") String areaId,
                                               @RequestBody PutFlightAreaParam param) {
        flightAreaService.updateFlightArea(workspaceId, areaId, param);
        return HttpResultResponse.success();
    }

    @PostMapping("/{workspace_id}/flight-area/sync")
    public HttpResultResponse syncFlightArea(@PathVariable(name = "workspace_id") String workspaceId,
                                             @RequestBody @Valid SyncFlightAreaParam param) {
        flightAreaService.syncFlightArea(workspaceId, param.getDeviceSns());
        return HttpResultResponse.success();
    }

}
