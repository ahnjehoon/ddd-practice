package com.jehoon.food.order.domain.exception;

import com.jehoon.food.domain.exception.DomainException;

/**
 * 주문 도메인 관련 모든 예외의 기본 클래스
 */
public abstract class OrderException extends DomainException {

    protected OrderException(String message) {
        super(message);
    }

    protected OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}