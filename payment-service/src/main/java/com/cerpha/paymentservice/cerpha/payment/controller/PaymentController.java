package com.cerpha.paymentservice.cerpha.payment.controller;

import com.cerpha.paymentservice.cerpha.payment.request.CompletePaymentRequest;
import com.cerpha.paymentservice.cerpha.payment.service.PaymentService;
import com.cerpha.paymentservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/complete")
    public ResponseEntity<ResultDto> completePayment(@RequestBody CompletePaymentRequest completePaymentRequest) {
        paymentService.completePayment(completePaymentRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
