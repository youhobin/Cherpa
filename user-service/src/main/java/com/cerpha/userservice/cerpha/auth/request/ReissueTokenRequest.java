package com.cerpha.userservice.cerpha.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReissueTokenRequest {

    @NotNull(message = "userId는 필수 입력 값 입니다.")
    private Long userId;

    @NotBlank(message = "refresh token은 필수 입력 값입니다.")
    private String refreshToken;
}
