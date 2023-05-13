package com.example.distributedlockpoc.order;

public enum OrderStatus {
    CREATED("Order created"),
    OUT_OF_STOCK("Out of stock"),
    FAILED("Failed to create order");

    private final String message;

    OrderStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
