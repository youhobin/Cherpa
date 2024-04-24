package com.cerpha.orderservice.cerpha.order.controller;

import com.cerpha.orderservice.cerpha.order.request.AddOrderRequest;
import com.cerpha.orderservice.cerpha.order.request.OrderListRequest;
import com.cerpha.orderservice.cerpha.order.response.OrderListResponse;
import com.cerpha.orderservice.cerpha.order.service.OrderService;
import com.cerpha.orderservice.common.dto.ResultDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResultDto> addOrder(@Valid @RequestBody AddOrderRequest addOrderRequest) {
        orderService.addOrder(addOrderRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ResultDto<OrderListResponse>> getOrderList(@Valid @RequestBody OrderListRequest orderListRequest) {
        OrderListResponse orderList = orderService.getOrderList(orderListRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, orderList));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResultDto> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

}