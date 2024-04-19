package com.cerpha.cerphaproject.cerpha.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpUserRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상 20글자 이하이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 50, message = "이름은 50자 이하이어야 합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(max = 50, message = "닉네임은 50자 이하이어야 합니다.")
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Size(max = 20, message = "전화번호는 20자 이하이어야 합니다.")
    private String phone;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    @Size(max = 100, message = "주소는 100자 이하이어야 합니다.")
    private String address;
}
