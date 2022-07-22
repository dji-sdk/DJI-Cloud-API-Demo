package com.dji.sample.manage.controller;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.manage.model.dto.UserDTO;
import com.dji.sample.manage.model.dto.UserLoginDTO;
import com.dji.sample.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.dji.sample.component.AuthInterceptor.PARAM_TOKEN;

@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}")
public class LoginController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginDTO loginDTO) {

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        return userService.userLogin(username, password, loginDTO.getFlag());
    }

    @PostMapping("/token/refresh")
    public ResponseResult refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(PARAM_TOKEN);
        Optional<UserDTO> user = userService.refreshToken(token);

        if (user.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return ResponseResult.error(CommonErrorEnum.NO_TOKEN.getErrorMsg());
        }

        return ResponseResult.success(user.get());
    }
}
