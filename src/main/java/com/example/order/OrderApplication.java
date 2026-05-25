package com.example.order;

import com.example.order.domain.service.OrderDomainService;
import com.example.order.domain.service.OrderNumberGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public OrderNumberGenerator orderNumberGenerator() {
        return new OrderNumberGenerator();
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainService();
    }
}