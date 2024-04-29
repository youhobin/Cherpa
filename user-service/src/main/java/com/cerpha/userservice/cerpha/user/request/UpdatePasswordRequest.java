package com.cerpha.userservice.cerpha.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상 20글자 이하이어야 합니다.")
    private String prevPassword;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상 20글자 이하이어야 합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상 20글자 이하이어야 합니다.")
    private String newPasswordCheck;

}
