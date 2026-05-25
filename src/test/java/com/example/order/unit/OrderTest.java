package com.example.order.unit;

import com.example.order.common.OrderErrorCode;
import com.example.order.common.OrderException;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order("ORD20240101000001", BigDecimal.ZERO);
    }

    @Test
    @DisplayName("添加订单项 - 成功")
    void should_add_item_successfully() {
        OrderItem item = new OrderItem("P001", "商品1", new BigDecimal("100.00"), 2);

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals("P001", order.getItems().get(0).getProductId());
    }

    @Test
    @DisplayName("添加重复产品ID - 抛出异常")
    void should_throw_exception_when_add_duplicate_product_id() {
        OrderItem item1 = new OrderItem("P001", "商品1", new BigDecimal("100.00"), 2);
        OrderItem item2 = new OrderItem("P001", "商品1", new BigDecimal("100.00"), 3);
        
        order.addItem(item1);

        OrderException exception = assertThrows(OrderException.class, () -> {
            order.addItem(item2);
        });

        assertEquals(OrderErrorCode.DUPLICATE_ORDER_ITEM, exception.getErrorCode());
    }

    @Test
    @DisplayName("添加数量为0的订单项 - 抛出异常")
    void should_throw_exception_when_quantity_is_zero() {
        OrderItem item = new OrderItem("P001", "商品1", new BigDecimal("100.00"), 0);

        OrderException exception = assertThrows(OrderException.class, () -> {
            order.addItem(item);
        });

        assertEquals(OrderErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    @DisplayName("添加数量为负数的订单项 - 抛出异常")
    void should_throw_exception_when_quantity_is_negative() {
        OrderItem item = new OrderItem("P001", "商品1", new BigDecimal("100.00"), -1);

        OrderException exception = assertThrows(OrderException.class, () -> {
            order.addItem(item);
        });

        assertEquals(OrderErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }
}