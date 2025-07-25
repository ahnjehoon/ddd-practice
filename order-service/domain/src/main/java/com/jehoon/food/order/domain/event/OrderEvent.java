package com.jehoon.food.order.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.domain.event.DomainEvent;
import com.jehoon.food.order.domain.entity.Order;

import lombok.Getter;

@Getter
public abstract class OrderEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }
}
