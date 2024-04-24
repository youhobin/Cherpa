package com.cerpha.orderservice.common.client.user;

import com.cerpha.orderservice.common.client.user.response.UserProfileResponse;
import com.cerpha.orderservice.common.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    ResultDto<UserProfileResponse> getUserProfile(@PathVariable("userId") Long userId);
}
