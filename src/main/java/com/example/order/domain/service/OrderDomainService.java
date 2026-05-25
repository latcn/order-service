package com.example.order.domain.service;

import com.example.order.domain.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderDomainService {

    public BigDecimal calculateTotal(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }
}