package com.example.order.unit;

import com.example.order.application.service.OrderApplicationService;
import com.example.order.common.OrderErrorCode;
import com.example.order.common.OrderException;
import com.example.order.domain.model.OrderItem;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.service.OrderDomainService;
import com.example.order.domain.service.OrderNumberGenerator;
import com.example.order.application.dto.CreateOrderRequest;
import com.example.order.application.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderDomainService orderDomainService;
    private OrderNumberGenerator orderNumberGenerator;
    private OrderApplicationService orderApplicationService;

    @BeforeEach
    void setUp() {
        orderDomainService = new OrderDomainService();
        orderNumberGenerator = new OrderNumberGenerator();
        orderApplicationService = new OrderApplicationService(
            orderRepository, orderDomainService, orderNumberGenerator
        );
    }

    @Test
    @DisplayName("第1轮：正常创建订单 - 两个不同productId的订单项，数量>0")
    void should_create_order_successfully_when_valid_request() {
        CreateOrderRequest.OrderItemRequest item1 = new CreateOrderRequest.OrderItemRequest(
            "P001", "商品1", new BigDecimal("100.00"), 2
        );
        CreateOrderRequest.OrderItemRequest item2 = new CreateOrderRequest.OrderItemRequest(
            "P002", "商品2", new BigDecimal("50.00"), 3
        );
        CreateOrderRequest request = new CreateOrderRequest(Arrays.asList(item1, item2));

        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderApplicationService.createOrder(request);

        assertNotNull(response);
        assertNotNull(response.getOrderNo());
        assertEquals(new BigDecimal("350.00"), response.getTotalAmount());
        assertEquals("CREATED", response.getStatus());
        assertEquals(2, response.getItems().size());
    }

    @Test
    @DisplayName("第2轮：重复产品ID校验 - 两个相同productId的订单项")
    void should_throw_exception_when_duplicate_product_id() {
        CreateOrderRequest.OrderItemRequest item1 = new CreateOrderRequest.OrderItemRequest(
            "P001", "商品1", new BigDecimal("100.00"), 2
        );
        CreateOrderRequest.OrderItemRequest item2 = new CreateOrderRequest.OrderItemRequest(
            "P001", "商品1", new BigDecimal("100.00"), 3
        );
        CreateOrderRequest request = new CreateOrderRequest(Arrays.asList(item1, item2));

        OrderException exception = assertThrows(OrderException.class, () -> {
            orderApplicationService.createOrder(request);
        });

        assertEquals(OrderErrorCode.DUPLICATE_ORDER_ITEM, exception.getErrorCode());
    }

    @Test
    @DisplayName("第3轮：数量<=0校验 - 订单项数量为0")
    void should_throw_exception_when_quantity_is_zero() {
        CreateOrderRequest.OrderItemRequest item1 = new CreateOrderRequest.OrderItemRequest(
            "P001", "商品1", new BigDecimal("100.00"), 0
        );
        CreateOrderRequest request = new CreateOrderRequest(Arrays.asList(item1));

        OrderException exception = assertThrows(OrderException.class, () -> {
            orderApplicationService.createOrder(request);
        });

        assertEquals(OrderErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    @DisplayName("第3轮：数量<=0校验 - 订单项数量为负数")
    void should_throw_exception_when_quantity_is_negative() {
        CreateOrderRequest.OrderItemRequest item1 = new CreateOrderRequest.OrderItemRequest(
            "P001", "商品1", new BigDecimal("100.00"), -1
        );
        CreateOrderRequest request = new CreateOrderRequest(Arrays.asList(item1));

        OrderException exception = assertThrows(OrderException.class, () -> {
            orderApplicationService.createOrder(request);
        });

        assertEquals(OrderErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }
}