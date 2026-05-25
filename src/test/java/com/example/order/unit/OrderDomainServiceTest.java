package com.example.order.unit;

import com.example.order.domain.model.OrderItem;
import com.example.order.domain.service.OrderDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDomainServiceTest {

    private OrderDomainService orderDomainService;

    @BeforeEach
    void setUp() {
        orderDomainService = new OrderDomainService();
    }

    @Test
    @DisplayName("第4轮：总金额计算 - 3个订单项")
    void should_calculate_total_correctly_for_three_items() {
        OrderItem item1 = new OrderItem("P001", "商品1", new BigDecimal("100.00"), 2);
        OrderItem item2 = new OrderItem("P002", "商品2", new BigDecimal("50.00"), 3);
        OrderItem item3 = new OrderItem("P003", "商品3", new BigDecimal("200.00"), 1);
        
        List<OrderItem> items = Arrays.asList(item1, item2, item3);

        BigDecimal total = orderDomainService.calculateTotal(items);

        assertEquals(new BigDecimal("550.00"), total);
    }

    @Test
    @DisplayName("第4轮：总金额计算 - 空列表")
    void should_return_zero_when_empty_items() {
        List<OrderItem> items = Arrays.asList();

        BigDecimal total = orderDomainService.calculateTotal(items);

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("第4轮：总金额计算 - 单个订单项")
    void should_calculate_total_correctly_for_single_item() {
        OrderItem item = new OrderItem("P001", "商品1", new BigDecimal("99.99"), 5);
        
        List<OrderItem> items = Arrays.asList(item);

        BigDecimal total = orderDomainService.calculateTotal(items);

        assertEquals(new BigDecimal("499.95"), total);
    }
}