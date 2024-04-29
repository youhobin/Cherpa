package com.cerpha.userservice.cerpha.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutRequest {

    @NotBlank(message = "access token은 필수 입력 값입니다.")
    private String accessToken;

    @NotBlank(message = "refresh token은 필수 입력 값입니다.")
    private String refreshToken;

}
