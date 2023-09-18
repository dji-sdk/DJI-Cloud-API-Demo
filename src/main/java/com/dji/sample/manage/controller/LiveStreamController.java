package com.dji.sample.manage.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.manage.model.dto.CapacityDeviceDTO;
import com.dji.sample.manage.model.dto.LiveTypeDTO;
import com.dji.sample.manage.service.ILiveStreamService;
import com.dji.sdk.common.HttpResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */

@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/live")
public class LiveStreamController {

    @Autowired
    private ILiveStreamService liveStreamService;

    @Autowired
    private ObjectMapper mapper;

    /**
     * Get live capability data of all drones in the current user's workspace from the database.
     * @param request
     * @return  live capability
     */
    @GetMapping("/capacity")
    public HttpResultResponse<List<CapacityDeviceDTO>> getLiveCapacity(HttpServletRequest request) {
        // Get information about the current user.
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);

        List<CapacityDeviceDTO> liveCapacity = liveStreamService.getLiveCapacity(customClaim.getWorkspaceId());

        return HttpResultResponse.success(liveCapacity);
    }

    /**
     * Live streaming according to the parameters passed in from the web side.
     * @param liveParam Live streaming parameters.
     * @return
     */
    @PostMapping("/streams/start")
    public HttpResultResponse liveStart(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveStart(liveParam);
    }

    /**
     * Stop live streaming according to the parameters passed in from the web side.
     * @param liveParam Live streaming parameters.
     * @return
     */
    @PostMapping("/streams/stop")
    public HttpResultResponse liveStop(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveStop(liveParam.getVideoId());
    }

    /**
     * Set the quality of the live streaming according to the parameters passed in from the web side.
     * @param liveParam Live streaming parameters.
     * @return
     */
    @PostMapping("/streams/update")
    public HttpResultResponse liveSetQuality(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveSetQuality(liveParam);
    }

    @PostMapping("/streams/switch")
    public HttpResultResponse liveLensChange(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveLensChange(liveParam);
    }

}