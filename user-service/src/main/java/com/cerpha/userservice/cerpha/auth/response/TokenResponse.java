package com.cerpha.userservice.cerpha.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

    private Long userId;
    private String accessToken;
}
