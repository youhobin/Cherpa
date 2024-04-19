package com.cerpha.cerphaproject.cerpha.user.request;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    private String prevPassword;
    private String newPassword;
    private String newPasswordCheck;

}
