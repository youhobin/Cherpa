package com.cerpha.orderservice.cerpha.order.controller;

import com.cerpha.orderservice.cerpha.order.service.OrderService;
import com.cerpha.orderservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/orders")
public class OrderInternalController {

    private final OrderService orderService;

    public OrderInternalController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 결제 완료된 주문
     * @param orderId
     * @return
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<ResultDto> completeOrderPayment(@PathVariable("orderId") Long orderId) {
        orderService.completeOrderPayment(orderId);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
