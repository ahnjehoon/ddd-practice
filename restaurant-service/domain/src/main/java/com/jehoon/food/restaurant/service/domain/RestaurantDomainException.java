package com.jehoon.food.restaurant.service.domain;

import com.jehoon.food.domain.exception.DomainException;

public class RestaurantDomainException extends DomainException {

    public RestaurantDomainException(String message) {
        super(message);
    }

    public RestaurantDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}