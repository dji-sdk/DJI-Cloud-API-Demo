package com.dji.sample.manage.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginDTO {

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private Integer flag;
}
