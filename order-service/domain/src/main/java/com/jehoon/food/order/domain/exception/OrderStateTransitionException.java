package com.jehoon.food.order.domain.exception;

import com.jehoon.food.domain.valueobject.OrderStatus;

/**
 * 주문 상태 전환 관련 예외
 */
public class OrderStateTransitionException extends OrderException {

    public OrderStateTransitionException(OrderStatus from, OrderStatus to) {
        super(String.format("잘못된 주문 상태 전환입니다: %s -> %s", from, to));
    }

    public OrderStateTransitionException(String message) {
        super(message);
    }
}