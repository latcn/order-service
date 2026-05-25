package com.example.order.interfaces.controller;

import com.example.order.application.service.OrderApplicationService;
import com.example.order.common.ApiResponse;
import com.example.order.application.dto.CreateOrderRequest;
import com.example.order.application.dto.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("Received create order request");
        OrderResponse response = orderApplicationService.createOrder(request);
        return ApiResponse.success(response);
    }
}