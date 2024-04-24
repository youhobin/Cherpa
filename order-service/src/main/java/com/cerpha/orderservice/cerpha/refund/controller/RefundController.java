package com.cerpha.orderservice.cerpha.refund.controller;

import com.cerpha.orderservice.cerpha.refund.request.RefundRequest;
import com.cerpha.orderservice.cerpha.refund.service.RefundService;
import com.cerpha.orderservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    /**
     * 주문 반품
     * @param refundRequest
     * @return
     */
    @PutMapping("/refund")
    public ResponseEntity<ResultDto> refundOrder(@RequestBody RefundRequest refundRequest) {
        refundService.refundOrder(refundRequest);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

}
