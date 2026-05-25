package com.example.order.common;

public enum OrderErrorCode {
    
    DUPLICATE_ORDER_ITEM("DUPLICATE_ORDER_ITEM", "同一商品在订单中不可重复"),
    INVALID_QUANTITY("INVALID_QUANTITY", "订单项数量必须大于0"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "订单不存在"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误");

    private final String code;
    private final String message;

    OrderErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}