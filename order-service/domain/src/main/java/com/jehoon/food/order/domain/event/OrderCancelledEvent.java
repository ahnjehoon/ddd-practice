package com.jehoon.food.order.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.order.domain.entity.Order;

public class OrderCancelledEvent extends OrderEvent {
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
