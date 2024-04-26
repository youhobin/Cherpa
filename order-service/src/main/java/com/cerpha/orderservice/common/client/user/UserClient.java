package com.cerpha.orderservice.common.client.user;

import com.cerpha.orderservice.common.dto.ResultDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/internal/users/{userId}")
    ResultDto<Long> getUserId(@PathVariable("userId") Long userId);
}
