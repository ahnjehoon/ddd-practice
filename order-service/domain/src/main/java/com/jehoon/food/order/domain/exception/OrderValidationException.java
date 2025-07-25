package com.jehoon.food.order.domain.exception;

/**
 * 주문 검증 관련 예외
 */
public class OrderValidationException extends OrderException {

    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}