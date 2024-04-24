package com.cerpha.userservice.cerpha.auth.request;

import lombok.Getter;

@Getter
public class LogoutRequest {

    private String accessToken;
    private String refreshToken;

}
