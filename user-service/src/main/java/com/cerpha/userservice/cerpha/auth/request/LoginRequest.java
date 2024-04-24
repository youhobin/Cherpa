package com.cerpha.userservice.cerpha.auth.request;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;
    private String password;
}
