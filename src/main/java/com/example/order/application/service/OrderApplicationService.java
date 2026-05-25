package com.example.order.application.service;

import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.service.OrderDomainService;
import com.example.order.domain.service.OrderNumberGenerator;
import com.example.order.application.dto.CreateOrderRequest;
import com.example.order.application.dto.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    private final OrderNumberGenerator orderNumberGenerator;

    public OrderApplicationService(OrderRepository orderRepository, 
                                   OrderDomainService orderDomainService,
                                   OrderNumberGenerator orderNumberGenerator) {
        this.orderRepository = orderRepository;
        this.orderDomainService = orderDomainService;
        this.orderNumberGenerator = orderNumberGenerator;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order with {} items", request.getItems().size());
        
        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            OrderItem item = new OrderItem(
                itemRequest.getProductId(),
                itemRequest.getProductName(),
                itemRequest.getUnitPrice(),
                itemRequest.getQuantity()
            );
            orderItems.add(item);
        }
        
        String orderNo = orderNumberGenerator.generate();
        Order order = new Order(orderNo, orderDomainService.calculateTotal(orderItems));
        order.addItems(orderItems);
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully, orderNo: {}", savedOrder.getOrderNo());
        
        return convertToResponse(savedOrder);
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        
        List<OrderResponse.OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity()
            );
            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);
        
        return response;
    }
}