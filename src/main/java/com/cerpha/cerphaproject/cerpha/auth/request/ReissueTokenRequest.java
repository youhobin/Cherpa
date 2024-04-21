package com.cerpha.cerphaproject.cerpha.auth.request;

import lombok.Getter;

@Getter
public class ReissueTokenRequest {

    private Long userId;
    private String refreshToken;
}
