package com.cerpha.cerphaproject.cerpha.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class EmailRequest {

    @Email(message = "올바른 이메일이 아닙니다.")
    private String email;

    @Min(value = 100000, message = "인증 번호는 6자리 숫자여야 합니다.")
    @Max(value = 999999, message = "인증 번호는 6자리 숫자여야 합니다.")
    private int authNumber;
}
