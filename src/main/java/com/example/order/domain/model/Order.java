package com.example.order.domain.model;

import com.example.order.common.OrderErrorCode;
import com.example.order.common.OrderException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 32)
    private String orderNo;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(String orderNo, BigDecimal totalAmount) {
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        if (item.getQuantity() <= 0) {
            throw new OrderException(OrderErrorCode.INVALID_QUANTITY);
        }
        
        for (OrderItem existingItem : items) {
            if (existingItem.getProductId().equals(item.getProductId())) {
                throw new OrderException(OrderErrorCode.DUPLICATE_ORDER_ITEM);
            }
        }
        
        items.add(item);
        item.setOrder(this);
    }

    public void addItems(List<OrderItem> items) {
        for (OrderItem item : items) {
            addItem(item);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}