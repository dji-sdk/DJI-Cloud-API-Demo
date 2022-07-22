package com.dji.sample.component.websocket.config;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.util.JwtUtil;
import com.dji.sample.component.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

/**
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Slf4j
@Component
public class AuthPrincipalHandler extends DefaultHandshakeHandler {

    @Override
    protected boolean isValidOrigin(ServerHttpRequest request) {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = servletRequest.getParameter(AuthInterceptor.PARAM_TOKEN);

            if (!StringUtils.hasText(token)) {
                return false;
            }
            log.debug("token:" + token);
            Optional<CustomClaim> customClaim = JwtUtil.parseToken(token);
            if (customClaim.isEmpty()) {
                return false;
            }

            servletRequest.setAttribute(AuthInterceptor.TOKEN_CLAIM, customClaim.get());
            return true;
        }
        return false;

    }

    /**
     * The principal's name: {workspaceId}/{userType}/{userId}
     * @param request
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {

            // get the custom claim
            CustomClaim claim = (CustomClaim) ((ServletServerHttpRequest) request).getServletRequest()
                    .getAttribute(AuthInterceptor.TOKEN_CLAIM);

            return () -> claim.getWorkspaceId() + "/" + claim.getUserType() + "/" + claim.getId();
        }
        return () -> null;
    }
}