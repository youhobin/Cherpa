package com.cerpha.orderservice.common.client.user.response;

import lombok.Getter;

@Getter
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String phone;
    private String address;

}
