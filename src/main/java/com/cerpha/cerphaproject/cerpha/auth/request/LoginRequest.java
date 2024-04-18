package com.cerpha.cerphaproject.cerpha.auth.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;
    private String password;
}
