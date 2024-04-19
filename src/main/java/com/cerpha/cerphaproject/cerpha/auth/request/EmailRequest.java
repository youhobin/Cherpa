package com.cerpha.cerphaproject.cerpha.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {

    @Email(message = "올바른 이메일이 아닙니다.")
    private String email;

    @Min(value = 100000, message = "6")
    private int authNumber;
}
