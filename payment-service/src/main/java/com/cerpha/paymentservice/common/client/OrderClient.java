package com.cerpha.paymentservice.common.client;


import com.cerpha.paymentservice.common.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PostMapping("/api/internal/orders/{orderId}/complete")
    ResultDto completeOrderPayment(@PathVariable("orderId") Long orderId);
}
