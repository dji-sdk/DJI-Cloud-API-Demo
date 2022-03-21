package com.dji.sample.manage.service;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.manage.model.dto.UserDTO;

import java.util.Optional;

public interface IUserService {

    /**
     * Query user's details based on username.
     * @param username
     * @param workspaceId
     * @return
     */
    ResponseResult getUserByUsername(String username, String workspaceId);

    /**
     * Verify the username and password to log in.
     * @param username
     * @param password
     * @return
     */
    ResponseResult userLogin(String username, String password);

    /**
     * Create a user object containing a new token.
     * @param token
     * @return
     */
    Optional<UserDTO> refreshToken(String token);
}
