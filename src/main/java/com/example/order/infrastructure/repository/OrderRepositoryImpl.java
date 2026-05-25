package com.example.order.infrastructure.repository;

import com.example.order.domain.model.Order;
import com.example.order.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepositoryImpl extends OrderRepository, JpaRepository<Order, Long> {

    @Override
    Optional<Order> findByOrderNo(String orderNo);
}