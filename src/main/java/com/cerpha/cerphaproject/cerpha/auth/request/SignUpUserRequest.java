package com.cerpha.cerphaproject.cerpha.auth.request;

import lombok.Getter;

@Getter
public class SignUpUserRequest {

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String address;
}
