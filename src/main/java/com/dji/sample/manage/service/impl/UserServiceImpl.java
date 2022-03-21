package com.dji.sample.manage.service.impl;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.common.util.JwtUtil;
import com.dji.sample.component.mqtt.config.MqttConfiguration;
import com.dji.sample.manage.dao.IUserMapper;
import com.dji.sample.manage.model.dto.UserDTO;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import com.dji.sample.manage.model.entity.UserEntity;
import com.dji.sample.manage.service.IUserService;
import com.dji.sample.manage.service.IWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper mapper;

    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private IWorkspaceService workspaceService;

    @Override
    public ResponseResult getUserByUsername(String username, String workspaceId) {

        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("invalid username")
                    .build();
        }

        UserDTO user = this.entityConvertToDTO(userEntity);
        user.setWorkspaceId(workspaceId);

        return ResponseResult.success(user);
    }

    @Override
    public ResponseResult userLogin(String username, String password) {
        // check user
        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("invalid username")
                    .build();
        }
        if (!password.equals(userEntity.getPassword())) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("invalid password")
                    .build();
        }

        Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceById(userEntity.getWorkspaceId());
        if (workspaceOpt.isEmpty()) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("invalid workspace id")
                    .build();
        }

        CustomClaim customClaim = new CustomClaim(userEntity.getUserId(),
                userEntity.getUsername(), userEntity.getUserType(),
                workspaceOpt.get().getWorkspaceId());

        // create token
        String token = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO userDTO = entityConvertToDTO(userEntity);
        userDTO.setMqttAddr(new StringBuilder()
                .append(mqttConfiguration.getProtocol().trim())
                .append("://")
                .append(mqttConfiguration.getHost().trim())
                .append(":")
                .append(mqttConfiguration.getPort())
                .toString());
        userDTO.setAccessToken(token);
        userDTO.setWorkspaceId(workspaceOpt.get().getWorkspaceId());
        return ResponseResult.success(userDTO);
    }

    @Override
    public Optional<UserDTO> refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Optional.empty();
        }
        CustomClaim customClaim = new CustomClaim(JWT.decode(token).getClaims());
        String refreshToken = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO user = entityConvertToDTO(this.getUserByUsername(customClaim.getUsername()));
        user.setWorkspaceId(customClaim.getWorkspaceId());
        user.setAccessToken(refreshToken);
        return Optional.of(user);
    }

    /**
     * Query a user by username.
     * @param username
     * @return
     */
    private UserEntity getUserByUsername(String username) {
        return mapper.selectOne(new QueryWrapper<UserEntity>()
                .eq("username", username));
    }

    private UserDTO entityConvertToDTO(UserEntity entity) {
        if (entity == null) {
            return new UserDTO();
        }
        return UserDTO.builder()
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .userType(entity.getUserType())
                .mqttUsername(entity.getMqttUsername())
                .mqttPassword(entity.getMqttPassword())
                .mqttAddr(new StringBuilder()
                        .append(mqttConfiguration.getProtocol().trim())
                        .append("://")
                        .append(mqttConfiguration.getHost().trim())
                        .append(":")
                        .append(mqttConfiguration.getPort())
                        .toString())
                .build();
    }
}
