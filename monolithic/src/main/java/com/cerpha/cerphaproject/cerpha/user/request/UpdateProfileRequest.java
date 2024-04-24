package com.cerpha.cerphaproject.cerpha.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    @Size(max = 100, message = "주소는 100자 이하이어야 합니다.")
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Size(max = 20, message = "전화번호는 20자 이하이어야 합니다.")
    private String phone;
}
