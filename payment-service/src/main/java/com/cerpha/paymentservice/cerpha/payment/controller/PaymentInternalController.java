package com.cerpha.paymentservice.cerpha.payment.controller;

import com.cerpha.paymentservice.cerpha.payment.request.ProcessPaymentRequest;
import com.cerpha.paymentservice.cerpha.payment.service.PaymentService;
import com.cerpha.paymentservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/payments")
public class PaymentInternalController {

    private final PaymentService paymentService;

    public PaymentInternalController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ResultDto> processPayment(@RequestBody ProcessPaymentRequest processPaymentRequest) {
        paymentService.processPayment(processPaymentRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
