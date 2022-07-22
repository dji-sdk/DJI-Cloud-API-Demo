package com.dji.sample.manage.service.impl;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.common.util.JwtUtil;
import com.dji.sample.component.mqtt.config.MqttConfiguration;
import com.dji.sample.manage.dao.IUserMapper;
import com.dji.sample.manage.model.dto.UserDTO;
import com.dji.sample.manage.model.dto.UserListDTO;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import com.dji.sample.manage.model.entity.UserEntity;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.IUserService;
import com.dji.sample.manage.service.IWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseResult userLogin(String username, String password, Integer flag) {
        // check user
        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("invalid username")
                    .build();
        }
        if (flag.intValue() != userEntity.getUserType().intValue()) {
            return ResponseResult.error("The account type does not match.");
        }
        if (!password.equals(userEntity.getPassword())) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("invalid password")
                    .build();
        }

        Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(userEntity.getWorkspaceId());
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

    @Override
    public PaginationData<UserListDTO> getUsersByWorkspaceId(long page, long pageSize, String workspaceId) {
        Page<UserEntity> userEntityPage = mapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getWorkspaceId, workspaceId));

        List<UserListDTO> usersList = userEntityPage.getRecords()
                .stream()
                .map(this::entity2UserListDTO)
                .collect(Collectors.toList());
        return new PaginationData<>(usersList, new Pagination(userEntityPage));
    }

    @Override
    public Boolean updateUser(String workspaceId, String userId, UserListDTO user) {
        UserEntity userEntity = mapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUserId, userId)
                        .eq(UserEntity::getWorkspaceId, workspaceId));
        if (userEntity == null) {
            return false;
        }
        userEntity.setMqttUsername(user.getMqttUsername());
        userEntity.setMqttPassword(user.getMqttPassword());
        userEntity.setUpdateTime(System.currentTimeMillis());
        int id = mapper.update(userEntity, new LambdaUpdateWrapper<UserEntity>()
                .eq(UserEntity::getUserId, userId)
                .eq(UserEntity::getWorkspaceId, workspaceId));

        return id > 0;
    }

    /**
     * Convert database entity objects into user data transfer object.
     * @param entity
     * @return
     */
    private UserListDTO entity2UserListDTO(UserEntity entity) {
        UserListDTO.UserListDTOBuilder builder = UserListDTO.builder();
        if (entity != null) {
            builder.userId(entity.getUserId())
                    .username(entity.getUsername())
                    .mqttUsername(entity.getMqttUsername())
                    .mqttPassword(entity.getMqttPassword())
                    .userType(UserTypeEnum.find(entity.getUserType()).getDesc())
                    .createTime(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(entity.getCreateTime()), ZoneId.systemDefault()));
            Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(entity.getWorkspaceId());
            workspaceOpt.ifPresent(workspace -> builder.workspaceName(workspace.getWorkspaceName()));
        }

        return builder.build();
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

    /**
     * Convert database entity objects into user data transfer object.
     * @param entity
     * @return
     */
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
