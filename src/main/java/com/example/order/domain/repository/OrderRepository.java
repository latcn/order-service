package com.example.order.domain.repository;

import com.example.order.domain.model.Order;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByOrderNo(String orderNo);

    void deleteById(Long id);
}