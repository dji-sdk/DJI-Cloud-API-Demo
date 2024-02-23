package com.dji.sample.manage.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.util.JwtUtil;
import com.dji.sample.component.mqtt.config.MqttPropertyConfiguration;
import com.dji.sample.manage.dao.IUserMapper;
import com.dji.sample.manage.model.dto.UserDTO;
import com.dji.sample.manage.model.dto.UserListDTO;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import com.dji.sample.manage.model.entity.UserEntity;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.IUserService;
import com.dji.sample.manage.service.IWorkspaceService;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.Pagination;
import com.dji.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper mapper;

    @Autowired
    private MqttPropertyConfiguration mqttPropertyConfiguration;

    @Autowired
    private IWorkspaceService workspaceService;

    @Override
    public HttpResultResponse getUserByUsername(String username, String workspaceId) {

        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid username");
        }

        UserDTO user = this.entityConvertToDTO(userEntity);
        user.setWorkspaceId(workspaceId);

        return HttpResultResponse.success(user);
    }

    @Override
    public HttpResultResponse userLogin(String username, String password, Integer flag) {
        // check user
        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid username");
        }
        if (flag.intValue() != userEntity.getUserType().intValue()) {
            return HttpResultResponse.error("The account type does not match.");
        }
        if (!password.equals(userEntity.getPassword())) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid password");
        }

        Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(userEntity.getWorkspaceId());
        if (workspaceOpt.isEmpty()) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid workspace id");
        }

        CustomClaim customClaim = new CustomClaim(userEntity.getUserId(),
                userEntity.getUsername(), userEntity.getUserType(),
                workspaceOpt.get().getWorkspaceId());

        // create token
        String token = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO userDTO = entityConvertToDTO(userEntity);
        userDTO.setMqttAddr(MqttPropertyConfiguration.getBasicMqttAddress());
        userDTO.setAccessToken(token);
        userDTO.setWorkspaceId(workspaceOpt.get().getWorkspaceId());
        return HttpResultResponse.success(userDTO);
    }

    @Override
    public Optional<UserDTO> refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Optional.empty();
        }
        CustomClaim customClaim;
        try {
            DecodedJWT jwt = JwtUtil.verifyToken(token);
            customClaim = new CustomClaim(jwt.getClaims());
        } catch (TokenExpiredException e) {
            customClaim = new CustomClaim(JWT.decode(token).getClaims());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        String refreshToken = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO user = entityConvertToDTO(this.getUserByUsername(customClaim.getUsername()));
        if (Objects.isNull(user)) {
            return Optional.empty();
        }
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
        return new PaginationData<>(usersList, new Pagination(userEntityPage.getCurrent(), userEntityPage.getSize(), userEntityPage.getTotal()));
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
            return null;
        }
        return UserDTO.builder()
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .userType(entity.getUserType())
                .mqttUsername(entity.getMqttUsername())
                .mqttPassword(entity.getMqttPassword())
                .mqttAddr(MqttPropertyConfiguration.getBasicMqttAddress())
                .build();
    }
}
