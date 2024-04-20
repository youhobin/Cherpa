package com.cerpha.cerphaproject.cerpha.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderListRequest {

    @NotNull(message = "유저 ID가 필요합니다.")
    private Long userId;
}
