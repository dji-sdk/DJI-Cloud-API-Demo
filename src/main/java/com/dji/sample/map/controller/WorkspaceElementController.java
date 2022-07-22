package com.dji.sample.map.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.map.model.dto.*;
import com.dji.sample.map.service.IWorkspaceElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@RestController
@RequestMapping("${url.map.prefix}${url.map.version}/workspaces")
public class WorkspaceElementController {

    @Autowired
    private IWorkspaceElementService elementService;

    @Autowired
    private ISendMessageService sendMessageService;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    /**
     * In the first connection, pilot will send out this http request to obtain the group element list.
     * Also, if pilot receives a group refresh instruction from WebSocket,
     * it needs the same interface to request the group element list.
     * @param workspaceId
     * @param groupId
     * @param isDistributed
     * @return
     */
    @GetMapping("/{workspace_id}/element-groups")
    public ResponseResult<List<GroupDTO>> getAllElements(@PathVariable(name = "workspace_id") String workspaceId,
                               @RequestParam(name = "group_id", required = false) String groupId,
                               @RequestParam(name = "is_distributed", required = false) Boolean isDistributed) {
        List<GroupDTO> groupsList = elementService.getAllGroupsByWorkspaceId(workspaceId, groupId, isDistributed);
        return ResponseResult.success(groupsList);
    }

    /**
     * When user draws a point, line or polygon on the PILOT/Web side.
     * Save the element information to the database.
     * @param request
     * @param workspaceId
     * @param groupId
     * @param elementCreate
     * @return
     */
    @PostMapping("/{workspace_id}/element-groups/{group_id}/elements")
    public ResponseResult saveElement(HttpServletRequest request,
                            @PathVariable(name = "workspace_id") String workspaceId,
                            @PathVariable(name = "group_id") String groupId,
                            @RequestBody ElementCreateDTO elementCreate) {
        CustomClaim claims = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        // Set the creator of the element
        elementCreate.getResource().setUsername(claims.getUsername());

        ResponseResult response = elementService.saveElement(groupId, elementCreate);
        if (response.getCode() != ResponseResult.CODE_SUCCESS) {
            return response;
        }

        // Notify all WebSocket connections in this workspace to be updated when an element is created.
        elementService.getElementByElementId(elementCreate.getId())
                .ifPresent(groupElement ->
                        sendMessageService.sendBatch(
                                webSocketManageService.getValueWithWorkspace(workspaceId),
                                CustomWebSocketMessage.<GroupElementDTO>builder()
                                        .timestamp(System.currentTimeMillis())
                                        .bizCode(BizCodeEnum.MAP_ELEMENT_CREATE.getCode())
                                        .data(groupElement)
                                        .build()));

        return ResponseResult.success(new ConcurrentHashMap<>(Map.of("id", elementCreate.getId())));
    }

    /**
     * When user edits a point, line or polygon on the PILOT/Web side.
     * Update the element information to the database.
     * @param request
     * @param workspaceId
     * @param elementId
     * @param elementUpdate
     * @return
     */
    @PutMapping("/{workspace_id}/elements/{element_id}")
    public ResponseResult updateElement(HttpServletRequest request,
                              @PathVariable(name = "workspace_id") String workspaceId,
                              @PathVariable(name = "element_id") String elementId,
                              @RequestBody ElementUpdateDTO elementUpdate) {

        CustomClaim claims = (CustomClaim) request.getAttribute(TOKEN_CLAIM);

        ResponseResult response = elementService.updateElement(elementId, elementUpdate, claims.getUsername());
        if (response.getCode() != ResponseResult.CODE_SUCCESS) {
            return response;
        }

        // Notify all WebSocket connections in this workspace to update when there is an element update.
        elementService.getElementByElementId(elementId)
                .ifPresent(groupElement ->
                        sendMessageService.sendBatch(
                                webSocketManageService.getValueWithWorkspace(workspaceId),
                                CustomWebSocketMessage.<GroupElementDTO>builder()
                                        .timestamp(System.currentTimeMillis())
                                        .bizCode(BizCodeEnum.MAP_ELEMENT_UPDATE.getCode())
                                        .data(groupElement)
                                        .build()));
        return response;
    }

    /**
     * When user delete a point, line or polygon on the PILOT/Web side,
     * Delete the element information in the database.
     * @param workspaceId
     * @param elementId
     * @return
     */
    @DeleteMapping("/{workspace_id}/elements/{element_id}")
    public ResponseResult deleteElement(@PathVariable(name = "workspace_id") String workspaceId,
                              @PathVariable(name = "element_id") String elementId) {

        Optional<GroupElementDTO> elementOpt = elementService.getElementByElementId(elementId);

        ResponseResult response = elementService.deleteElement(elementId);

        // Notify all WebSocket connections in this workspace to update when an element is deleted.
        if (ResponseResult.CODE_SUCCESS == response.getCode()) {
            elementOpt.ifPresent(element ->
                    sendMessageService.sendBatch(
                    webSocketManageService.getValueWithWorkspace(workspaceId),
                            CustomWebSocketMessage.<WebSocketElementDelDTO>builder()
                                    .timestamp(System.currentTimeMillis())
                                    .bizCode(BizCodeEnum.MAP_ELEMENT_DELETE.getCode())
                                    .data(WebSocketElementDelDTO.builder()
                                            .elementId(elementId)
                                            .groupId(element.getGroupId())
                                            .build())
                                    .build()));
        }
        return response;
    }

    /**
     * Delete all the element information in this group based on the group id.
     * @param workspaceId
     * @param groupId
     * @return
     */
    @DeleteMapping("/{workspace_id}/element-groups/{group_id}/elements")
    public ResponseResult deleteAllElementByGroupId(@PathVariable(name = "workspace_id") String workspaceId,
                                          @PathVariable(name = "group_id") String groupId) {

        ResponseResult response = elementService.deleteAllElementByGroupId(groupId);

        // Notify all WebSocket connections in this workspace to update when elements are deleted.
        if (ResponseResult.CODE_SUCCESS == response.getCode()) {

            sendMessageService.sendBatch(
                    webSocketManageService.getValueWithWorkspace(workspaceId),
                    CustomWebSocketMessage.builder()
                            .timestamp(System.currentTimeMillis())
                            .bizCode(BizCodeEnum.MAP_GROUP_REFRESH.getCode())
                            // Group ids that need to re-request data
                            .data(new ConcurrentHashMap<String, String[]>(Map.of("ids", new String[]{groupId})))
                            .build());
        }
        return response;
    }
}