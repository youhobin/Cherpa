package com.cerpha.userservice.cerpha.user.response;

import com.cerpha.userservice.cerpha.user.domain.Users;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String phone;
    private String address;

    public UserProfileResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.address = user.getAddress();
    }
}
