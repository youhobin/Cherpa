package com.cerpha.cerphaproject.cerpha.order.controller;

import com.cerpha.cerphaproject.cerpha.order.request.AddOrderRequest;
import com.cerpha.cerphaproject.cerpha.order.service.OrderService;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResultDto> addOrder(@RequestBody AddOrderRequest addOrderRequest) {
        orderService.addOrder(addOrderRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }


}
