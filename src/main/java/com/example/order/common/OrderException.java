package com.example.order.common;

public class OrderException extends RuntimeException {

    private final OrderErrorCode errorCode;

    public OrderException(OrderErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public OrderException(OrderErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public OrderErrorCode getErrorCode() {
        return errorCode;
    }
}