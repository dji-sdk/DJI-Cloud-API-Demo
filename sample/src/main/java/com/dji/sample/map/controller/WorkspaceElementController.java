package com.dji.sample.map.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.map.service.IWorkspaceElementService;
import com.dji.sdk.cloudapi.map.CreateMapElementRequest;
import com.dji.sdk.cloudapi.map.CreateMapElementResponse;
import com.dji.sdk.cloudapi.map.GetMapElementsResponse;
import com.dji.sdk.cloudapi.map.UpdateMapElementRequest;
import com.dji.sdk.cloudapi.map.api.IHttpMapService;
import com.dji.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@RestController
public class WorkspaceElementController implements IHttpMapService {

    @Autowired
    private IWorkspaceElementService elementService;

    @Autowired
    private IWebSocketMessageService sendMessageService;

    /**
     * Delete all the element information in this group based on the group id.
     * @param workspaceId
     * @param groupId
     * @return
     */
    @DeleteMapping("${url.map.prefix}${url.map.version}/workspaces/{workspace_id}/element-groups/{group_id}/elements")
    public HttpResultResponse deleteAllElementByGroupId(@PathVariable(name = "workspace_id") String workspaceId,
                                                        @PathVariable(name = "group_id") String groupId) {

        return elementService.deleteAllElementByGroupId(workspaceId, groupId);
    }

    /**
     * In the first connection, pilot will send out this http request to obtain the group element list.
     * Also, if pilot receives a group refresh instruction from WebSocket,
     * it needs the same interface to request the group element list.
     * @param workspaceId
     * @param groupId
     * @param isDistributed
     * @return
     */
    @Override
    public HttpResultResponse<List<GetMapElementsResponse>> getMapElements(String workspaceId, String groupId, Boolean isDistributed, HttpServletRequest req, HttpServletResponse rsp) {
        List<GetMapElementsResponse> groupsList = elementService.getAllGroupsByWorkspaceId(workspaceId, groupId, isDistributed);
        return HttpResultResponse.<List<GetMapElementsResponse>>success(groupsList);
    }

    /**
     * When user draws a point, line or polygon on the PILOT/Web side.
     * Save the element information to the database.
     * @param workspaceId
     * @param groupId
     * @param elementCreate
     * @return
     */
    @Override
    public HttpResultResponse<CreateMapElementResponse> createMapElement(String workspaceId, String groupId,
                     @Valid CreateMapElementRequest elementCreate, HttpServletRequest req, HttpServletResponse rsp) {
        CustomClaim claims = (CustomClaim) req.getAttribute(TOKEN_CLAIM);
        // Set the creator of the element
        elementCreate.getResource().setUsername(claims.getUsername());

        HttpResultResponse response = elementService.saveElement(workspaceId, groupId, elementCreate, true);
        if (response.getCode() != HttpResultResponse.CODE_SUCCESS) {
            return response;
        }

        return HttpResultResponse.success(new CreateMapElementResponse().setId(elementCreate.getId()));
    }

    /**
     * When user edits a point, line or polygon on the PILOT/Web side.
     * Update the element information to the database.
     * @param workspaceId
     * @param elementId
     * @param elementUpdate
     * @return
     */
    @Override
    public HttpResultResponse updateMapElement(String workspaceId, String elementId, @Valid UpdateMapElementRequest elementUpdate, HttpServletRequest req, HttpServletResponse rsp) {
        CustomClaim claims = (CustomClaim) req.getAttribute(TOKEN_CLAIM);

        HttpResultResponse response = elementService.updateElement(workspaceId, elementId, elementUpdate, claims.getUsername(), true);
        if (response.getCode() != HttpResultResponse.CODE_SUCCESS) {
            return response;
        }

        return response;
    }

    /**
     * When user delete a point, line or polygon on the PILOT/Web side,
     * Delete the element information in the database.
     * @param workspaceId
     * @param elementId
     * @return
     */
    @Override
    public HttpResultResponse deleteMapElement(String workspaceId, String elementId, HttpServletRequest req, HttpServletResponse rsp) {

        return elementService.deleteElement(workspaceId, elementId, true);
    }
}