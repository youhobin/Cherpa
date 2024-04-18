package com.cerpha.cerphaproject.cerpha.user.response;

import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String phone;
    private String address;

    public UserResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.address = user.getAddress();
    }
}
