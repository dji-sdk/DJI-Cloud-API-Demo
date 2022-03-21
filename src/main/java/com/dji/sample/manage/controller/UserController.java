package com.dji.sample.manage.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;


@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/current")
    public ResponseResult getCurrentUserInfo(HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);
        return userService.getUserByUsername(customClaim.getUsername(), customClaim.getWorkspaceId());
    }

}
