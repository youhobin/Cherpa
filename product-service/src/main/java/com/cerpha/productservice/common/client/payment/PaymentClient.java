package com.cerpha.productservice.common.client.payment;

import com.cerpha.productservice.common.client.payment.request.ProcessPaymentRequest;
import com.cerpha.productservice.common.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/internal/payments")
    ResultDto processPayment(ProcessPaymentRequest processPaymentRequest);
}
