package com.example.order.domain.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderNumberGenerator {

    private static final String PREFIX = "ORD";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Random RANDOM = new Random();

    public String generate() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%06d", RANDOM.nextInt(1000000));
        return PREFIX + datePart + randomPart;
    }
}