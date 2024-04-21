package com.cerpha.cerphaproject.cerpha.auth.response;

import lombok.Getter;

@Getter
public class VerifyEmailResponse {

    private boolean isVerified;

    public VerifyEmailResponse(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
