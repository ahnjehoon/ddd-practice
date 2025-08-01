package com.jehoon.food.restaurant.service.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.domain.event.DomainEvent;
import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.restaurant.service.domain.Restaurant;

import lombok.Getter;

@Getter
public class OrderRejectedEvent implements DomainEvent<Restaurant> {
    private final Restaurant restaurant;
    private final OrderId orderId;
    private final String rejectionReason;
    private final ZonedDateTime createdAt;

    public OrderRejectedEvent(Restaurant restaurant, OrderId orderId, String rejectionReason, ZonedDateTime createdAt) {
        this.restaurant = restaurant;
        this.orderId = orderId;
        this.rejectionReason = rejectionReason;
        this.createdAt = createdAt;
    }
}