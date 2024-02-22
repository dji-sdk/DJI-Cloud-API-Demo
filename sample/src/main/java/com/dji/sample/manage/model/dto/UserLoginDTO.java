package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private Integer flag;
}
